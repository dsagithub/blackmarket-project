package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.codec.digest.DigestUtils;

import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Black;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Comentario;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.ComentarioCollection;

@Path("/comentarios")
public class ComentarioResource {

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	 //Las querys para la base de datos

	private String GET_COMENTARIOS_QUERY = "SELECT * FROM comentarios";
	private String GET_COMENTARIOS_ID_QUERY = "select * from comentarios where id_comentario=?";
	private String GET_COMENTARIOS_ID_CONTENIDO_QUERY = "select * from comentarios where id_contenido=?";
	private String INSERT_COMENTARIOS_QUERY = "insert into comentarios (autor,id_contenido,comentario) values (?,?,?)";
	private String UPDATE_COMENTARIOS_QUERY = "update comentarios set comentario=ifnull(?, comentario) where id_comentario=?";
	private String DELETE_COMENTARIOS_QUERY = "delete from comentarios where id_comentario=?";

	@Context
	private SecurityContext security;
	
	
	
	//funcion que pinta todos los comentarios
	@GET
	@Produces(MediaType2.BLACKS_API_COMENTARIO_COLLECTION)
	public ComentarioCollection getComentarios() {

		ComentarioCollection comentarios = new ComentarioCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_COMENTARIOS_QUERY);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Comentario comentary = new Comentario();
				comentary.setId_comentario(rs.getInt("id_comentario"));
				comentary.setAutor(rs.getString("autor"));
				comentary.setId_contenido(rs.getString("id_contenido"));
				comentary.setComentario(rs.getString("comentario"));
				comentarios.addComentario(comentary);

			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return comentarios;
	}

	
	//Funcion que busca en la base de datos un comentario por el id
	private Comentario getComentarioFromDatabase(String idcomentario) {
		Comentario comentario = new Comentario();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_COMENTARIOS_ID_QUERY);
			stmt.setInt(1, Integer.valueOf(idcomentario));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				comentario.setId_comentario(rs.getInt("id_comentario"));
				comentario.setAutor(rs.getString("autor"));
				comentario.setId_contenido(rs.getString("id_contenido"));
				comentario.setComentario(rs.getString("comentario"));
			} else {
				throw new NotFoundException("There's no comentario with idcomentario="
						+ idcomentario);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return comentario;
	}

	
	
	//Pinta el comentario por id que busco con la funcion "getComentarioFromDatabase"
	@GET
	@Path("/{idcomentario}")
	@Produces(MediaType2.BLACKS_API_COMENTARIO)
	public Response getComentarioUser(
			@PathParam("idcomentario") String idcomentario,
			@Context Request request) {
		Comentario comentario = new Comentario();
		CacheControl cc = new CacheControl();
		comentario = getComentarioFromDatabase(idcomentario);
		 String referencia =(comentario.getComentario());
		 EntityTag eTag = new EntityTag(referencia);
		 Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);
		 if (rb != null) {
			 return rb.cacheControl(cc).tag(eTag).build();
		 }
		 rb = Response.ok(comentario).cacheControl(cc).tag(eTag);
		return rb.build();
	}

	
	//Pinta todos los comentarios de un black 
	@GET
	@Path("/contenido/{idcontenido}")
	@Produces(MediaType2.BLACKS_API_COMENTARIO)
	public ComentarioCollection getComentarioIdcontenido(
			@PathParam("idcontenido") String idcontenido,
			@Context Request request) {
		ComentarioCollection comentarios = new ComentarioCollection();
		

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_COMENTARIOS_ID_CONTENIDO_QUERY);
			stmt.setString(1, idcontenido);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Comentario comentary = new Comentario();
				comentary.setId_comentario(rs.getInt("id_comentario"));
				comentary.setAutor(rs.getString("autor"));
				comentary.setId_contenido(rs.getString("id_contenido"));
				comentary.setComentario(rs.getString("comentario"));
				comentarios.addComentario(comentary);

			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return comentarios;
	}

	
	//Crea un nuevo comentario 
	@POST
	@Consumes(MediaType2.BLACKS_API_COMENTARIO)
	@Produces(MediaType2.BLACKS_API_COMENTARIO)
	public Comentario createComentario(Comentario comentario) {
		validateComentario(comentario);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(INSERT_COMENTARIOS_QUERY,
					Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, comentario.getAutor());
			stmt.setString(2, comentario.getId_contenido());
			stmt.setString(3, comentario.getComentario());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return comentario;
	}

	private void validateComentario(Comentario comentario) {
		if (comentario.getAutor() == null)
			throw new BadRequestException("autor can't be null.");
		if (comentario.getId_contenido() == null)
			throw new BadRequestException("id_contenido can't be null.");
		if (comentario.getComentario() == null)
			throw new BadRequestException("comentario can't be null.");

	}

	
	//Borra un comentario dandole la id 
	@DELETE
	@Path("/{idcomentario}")
	public void deleteComentario(@PathParam("idcomentario") String idcomentario) {
		// validateUser(idasignatura);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_COMENTARIOS_QUERY);
			stmt.setInt(1, Integer.valueOf(idcomentario));

			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException(
						"There's no comentario with idcomentario=" + idcomentario);// Deleting
																				// inexistent
																				// sting
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

//modifica un comentario ( comentario)
	
	@PUT
	@Path("/{idcomentario}")
	@Consumes(MediaType2.BLACKS_API_COMENTARIO)
	@Produces(MediaType2.BLACKS_API_COMENTARIO)
	public Comentario updateComentario(
			@PathParam("idcomentario") String idcomentario,
			Comentario comentario) {
		 validateUser(idcomentario);
		validateUpdateComentario(comentario);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(UPDATE_COMENTARIOS_QUERY);
			stmt.setString(1, comentario.getComentario());
			stmt.setInt(2, Integer.valueOf(idcomentario));

			int rows = stmt.executeUpdate();
			if (rows == 1)
				comentario = getComentarioFromDatabase(idcomentario);
			else {
				throw new NotFoundException(
						"There's no comentario with id_comentario=" + idcomentario);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return comentario;
	}

	private void validateUpdateComentario(Comentario comentario) {
		if (comentario.getComentario() != null
				&& comentario.getComentario().length() > 100)
			throw new BadRequestException(
					"Comentario can't be greater than 100 characters.");

	}
	
	private void validateUser(String idcomentario) {
		Comentario comentario = getComentarioFromDatabase(idcomentario);
		String username = comentario.getAutor();
		if (!security.getUserPrincipal().getName().equals(username))
			throw new ForbiddenException(
					"You are not allowed to modify this comentario.");
	}

}
