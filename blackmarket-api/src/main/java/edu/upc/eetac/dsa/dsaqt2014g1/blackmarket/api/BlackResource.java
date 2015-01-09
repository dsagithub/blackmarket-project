






















package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


















import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Asignatura;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Black;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.BlackCollection;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Matricula;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.MatriculaCollection;





@Path("/blacks")
public class BlackResource {
	
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	private String GET_BLACK_QUERY_ASIGNATURA = "SELECT * FROM contenidos where id_asignatura=? and id_tipo=?";
	private String GET_BLACK_QUERY = "SELECT * FROM contenidos where id_contenido=?";

	private String GET_BLACK_QUERY_CONTENIDO = "SELECT * FROM contenidos";
	private String GET_BLACK_QUERY_MATRICULADAS = "select * from contenidos, users_matriculas where username_matriculas=? and id_asignatura_u_matriculas=id_asignatura order by fecha desc limit 5";
	private String GET_BLACK_QUERY_TITULO = "SELECT * FROM contenidos where titulo=?";
	
	private String GET_BLACK_QUERY_AUTOR_FROM_LAST = "select c.* from contenidos c where autor LIKE ? and c.fecha > ? order by fecha";
	private String GET_BLACKS_QUERY_AUTOR = "select c.* from contenidos c where autor LIKE ? and c.fecha < ifnull ( ?, now()) order by fecha desc limit ?";
	private String GET_BLACK_QUERY_TITULO_FROM_LAST = "select c.* from contenidos c where titulo LIKE ? and c.fecha > ? order by fecha";
	private String GET_BLACKS_QUERY_TITULO = "select c.* from contenidos c where titulo LIKE ? and c.fecha < ifnull ( ?, now()) order by fecha desc limit ?";
	
	
	
	private String GET_BLACK_QUERY_USUARIO = "SELECT * FROM contenidos where id_usuario=?";
	private String INSERT_BLACK_QUERY = "insert into contenidos (id_contenido,id_asignatura,id_tipo,titulo,descripcion,autor,invalid) values (?,?,?,?,?,?,'0')";
	private String DELETE_BLACK_QUERY = "delete from contenidos where id_contenido=?";
	private String UPDATE_BLACK_QUERY= "update contenidos set  titulo=ifnull(?, titulo), descripcion=ifnull(?, descripcion), autor=ifnull(?, autor) where id_contenido=?";
	private String UPDATE_INVALID_QUERY= "update contenidos set  invalid=invalid+1 where id_contenido=?";
	//@Context
	//private SecurityContext security;
	
	@GET
	@Produces(MediaType2.BLACKS_API_BLACK_COLLECTION)
	public BlackCollection getBlacks() {
		BlackCollection blacks = new BlackCollection();
	 
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_BLACK_QUERY_CONTENIDO);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Black black = new Black();
				black.setId_contenido(rs.getString("id_contenido"));
				black.setId_asignatura(rs.getInt("id_asignatura"));
				black.setId_tipo(rs.getInt("id_tipo"));
				black.setTitulo(rs.getString("titulo"));
				black.setDescripcion(rs.getString("descripcion"));
				black.setFecha(rs.getString("fecha"));
				black.setAutor(rs.getString("autor"));
				black.setInvalid(rs.getInt("invalid"));
				blacks.addBlack(black);
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
		return blacks;
	}
	
	private BlackCollection getBlacksFromDatabase(String username) {
		BlackCollection blacks = new BlackCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_BLACK_QUERY_MATRICULADAS);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Black black = new Black();
				//matricula.setUsername_matriculas(rs.getString("username_matriculas"));
				black.setId_contenido(rs.getString("id_contenido"));
				black.setId_asignatura(rs.getInt("id_asignatura"));
				black.setId_tipo(rs.getInt("id_tipo"));
				black.setTitulo(rs.getString("titulo"));
				black.setDescripcion(rs.getString("descripcion"));
				black.setAutor(rs.getString("autor"));
				black.setFecha(rs.getString("fecha"));
				blacks.addBlack(black);
				
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

		return blacks;
	}
	
	
	@GET
	@Path("/{username}")
	@Produces(MediaType2.BLACKS_API_BLACK_COLLECTION)
	public BlackCollection getBlacksUser(@PathParam("username") String username, @Context Request request) {
		BlackCollection blacks = new BlackCollection();
		//CacheControl cc = new CacheControl();
		blacks = getBlacksFromDatabase(username);		
		//String referencia = DigestUtils.md5Hex(matriculas.setUsername_matriculas());
		//EntityTag eTag = new EntityTag(referencia);
		//Response.ResponseBuilder rb = request.evaluatePreconditions(eTag); 
		//if (rb != null) {
			//return rb.cacheControl(cc).tag(eTag).build();
		//}
		//rb = Response.ok(matriculas).cacheControl(cc).tag(eTag);	 
		return blacks;
	}
	
	
	
	
	
	@GET
	@Path("contenido/{idcontenido}")
	@Produces(MediaType2.BLACKS_API_BLACK_COLLECTION)
	public Black getBlacksPorId(@PathParam("idcontenido") String idcontenido, @Context Request request) {
		Black black = new Black();
		//CacheControl cc = new CacheControl();
		black = getBlackFromDatabase(idcontenido);		
		//String referencia = DigestUtils.md5Hex(matriculas.setUsername_matriculas());
		//EntityTag eTag = new EntityTag(referencia);
		//Response.ResponseBuilder rb = request.evaluatePreconditions(eTag); 
		//if (rb != null) {
			//return rb.cacheControl(cc).tag(eTag).build();
		//}
		//rb = Response.ok(matriculas).cacheControl(cc).tag(eTag);	 
		return black;
	}
	
	
	


	@Context
	private Application app;
	private SecurityContext security;
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Black PostBlack(@FormDataParam("titulo") String titulo,
			@FormDataParam("id_tipo") int id_tipo,
			@FormDataParam("id_asignatura") int id_asignatura,
			@FormDataParam("descripcion") String descripcion,
			@FormDataParam("autor") String autor,
			@FormDataParam("id_contenido") InputStream id_contenido,
			@FormDataParam("id_contenido") FormDataContentDisposition fileDisposition) {
		UUID uuid = writeAndConvertImage(id_contenido);
		//int a=0;
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(INSERT_BLACK_QUERY);
			stmt.setString(1, uuid.toString());
			stmt.setInt(2, id_asignatura);
			stmt.setInt(3, id_tipo);
			stmt.setString(4, titulo);
			stmt.setString(5, descripcion);
			stmt.setString(6, autor);
			//stmt.setInt(7,a);
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
		Black black = new Black();
		
		
		black.setId_contenido(uuid.toString()+".png");

		return black;
	}
	
	
	private UUID writeAndConvertImage(InputStream file) {

		BufferedImage image = null;
		try {
			image = ImageIO.read(file);

		} catch (IOException e) {
			throw new InternalServerErrorException(
					"Something has been wrong when reading the file.");
		}
		UUID uuid = UUID.randomUUID();
		String filename = uuid.toString() + ".png";
		try {
			ImageIO.write(
					image,
					"png",
					new File(app.getProperties().get("uploadFolder") + filename));
		} catch (IOException e) {
			throw new InternalServerErrorException(
					"Something has been wrong when converting the file.");
		}

		return uuid;
	}
	
/*
	private void validateSting(Black sting) {
		if (sting.getSubject() == null)
			throw new BadRequestException("Subject can't be null.");
		if (sting.getContent() == null)
			throw new BadRequestException("Content can't be null.");
		if (sting.getSubject().length() > 100)
			throw new BadRequestException(
					"Subject can't be greater than 100 characters.");
		if (sting.getContent().length() > 500)
			throw new BadRequestException(
					"Content can't be greater than 500 characters.");
	}
	
	*/
	
	
	private Black getBlackFromDatabase(String idcontenido) {
		Black black = new Black();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_BLACK_QUERY );
			stmt.setString(1, idcontenido);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				
				black.setId_contenido(rs.getString("id_contenido"));
				black.setId_asignatura(rs.getInt("id_asignatura"));
				black.setId_tipo(rs.getInt("id_tipo"));
				black.setTitulo(rs.getString("titulo"));
				black.setDescripcion(rs.getString("descripcion"));
				black.setFecha(rs.getString("fecha"));
				black.setAutor(rs.getString("autor"));
				black.setInvalid(rs.getInt("invalid"));
			} else {
				throw new NotFoundException("There's no sting with stingid="
						+ idcontenido);
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

		return black;
	}	
	
	
	
	


	@GET
	@Path("/search/autor/{nombreautor}")
	@Produces(MediaType2.BLACKS_API_BLACK_COLLECTION)
	public BlackCollection getBlackAutor(
			@PathParam("nombreautor") String nombreautor,
			@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after) {
		BlackCollection coleccionblack = new BlackCollection();
		coleccionblack.setPattern(nombreautor);

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			stmt = updateFromLast ? conn
					.prepareStatement(GET_BLACK_QUERY_AUTOR_FROM_LAST ) : conn
					.prepareStatement(GET_BLACKS_QUERY_AUTOR);
			stmt.setString(1, '%' + nombreautor + '%');

			if (updateFromLast) {
				stmt.setTimestamp(2, new Timestamp(after));

			} else {
				if (before > 0) {
					stmt.setTimestamp(2, new Timestamp(before));

				} else
					stmt.setTimestamp(2, null);
				length = (length <= 0) ? 5 : length;
				stmt.setInt(3, length);
			}

			ResultSet rs = stmt.executeQuery();

			boolean first = true;
			long oldestTimestamp = 0;
			while (rs.next()) {
				Black black = new Black();
				black.setId_contenido(rs.getString("id_contenido"));
				black.setId_asignatura(rs.getInt("id_asignatura"));
				black.setId_tipo(rs.getInt("id_tipo"));
				black.setTitulo(rs.getString("titulo"));
				black.setDescripcion(rs.getString("descripcion"));
				black.setAutor(rs.getString("autor"));
				//black.setLink(rs.getString("link"));
				black.setInvalid(rs.getInt("invalid"));
				black.setFecha(rs.getString("fecha"));
				//oldestTimestamp = rs.getTimestamp("fecha").getTime();
				//black.setFecha(oldestTimestamp);
				//if (first) {
					//first = false;
					//coleccionblack.setNewestTimestamp(black.getFecha());
				//}
				coleccionblack.addBlack(black);
			}
			coleccionblack.setOldestTimestamp(oldestTimestamp);

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

		return coleccionblack;
	}

@GET
@Path("/search/titulo/{titulo}")
@Produces(MediaType2.BLACKS_API_BLACK_COLLECTION)
public BlackCollection getBlackTitulo(
		@PathParam("titulo") String titulo,
		@QueryParam("length") int length,
		@QueryParam("before") long before, @QueryParam("after") long after){
	BlackCollection coleccionblack = new BlackCollection();
	coleccionblack.setPattern(titulo);

	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
	PreparedStatement stmt = null;
	try {
		boolean updateFromLast = after > 0;
		stmt = updateFromLast ? conn
				.prepareStatement(GET_BLACK_QUERY_TITULO_FROM_LAST ) : conn
				.prepareStatement(GET_BLACKS_QUERY_TITULO);
		stmt.setString(1, '%' + titulo + '%');

		if (updateFromLast) {
			stmt.setTimestamp(2, new Timestamp(after));

		} else {
			if (before > 0) {
				stmt.setTimestamp(2, new Timestamp(before));

			} else
				stmt.setTimestamp(2, null);
			length = (length <= 0) ? 5 : length;
			stmt.setInt(3, length);
		}

		ResultSet rs = stmt.executeQuery();

		boolean first = true;
		long oldestTimestamp = 0;
		while (rs.next()) {
			Black black = new Black();
			black.setId_contenido(rs.getString("id_contenido"));
			black.setId_asignatura(rs.getInt("id_asignatura"));
			black.setId_tipo(rs.getInt("id_tipo"));
			black.setTitulo(rs.getString("titulo"));
			black.setDescripcion(rs.getString("descripcion"));
			black.setAutor(rs.getString("autor"));
			//black.setLink(rs.getString("link"));
			black.setInvalid(rs.getInt("invalid"));
			black.setFecha(rs.getString("fecha"));
			/*oldestTimestamp = rs.getTimestamp("fecha").getTime();
			black.setFecha(oldestTimestamp);
			if (first) {
				first = false;
				coleccionblack.setNewestTimestamp(black.getFecha());
			}*/
			coleccionblack.addBlack(black);
		}
		coleccionblack.setOldestTimestamp(oldestTimestamp);

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

	return coleccionblack;
}



@DELETE
@Path("/{idcontenido}")
public void deleteBlack(@PathParam("idcontenido") String idcontenido) {
	//validateUser(idasignatura);
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}

	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(DELETE_BLACK_QUERY);
		stmt.setString(1, idcontenido);

		int rows = stmt.executeUpdate();
		if (rows == 0)
			throw new NotFoundException("There's no sting with stingid="
					+ idcontenido);// Deleting inexistent sting
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


@PUT
@Path("/{idcontenido}")
@Consumes(MediaType2.BLACKS_API_BLACK)
@Produces(MediaType2.BLACKS_API_BLACK)
public Black updateBlack(@PathParam("idcontenido") String idcontenido, Black black) {
	//validateUser(stingid);
	validateUpdateBlack(black);
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}

	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(UPDATE_BLACK_QUERY);
		stmt.setString(1, black.getTitulo());
		stmt.setString(2, black.getDescripcion());
		stmt.setString(3, black.getAutor());
		stmt.setString(4, idcontenido);

		int rows = stmt.executeUpdate();
		if (rows == 1)
			black = getBlackFromDatabase(idcontenido);
		else {
			throw new NotFoundException("There's no sting with id_asignatura="
					+ idcontenido);
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

	return black;
}


private void validateUpdateBlack(Black black) {
	/*if (black.getId_asignatura() != null && black.getId_asignatura().length() > 20)
		throw new BadRequestException(
				"Nombre can't be greater than 20 characters.");
	if (black.getCurso() != null && black.getCurso().length() > 4)
		throw new BadRequestException(
				"Curso can't be greater than 4 characters.");*/
}


@PUT
@Path("/invalid/{idcontenido}")
@Consumes(MediaType2.BLACKS_API_BLACK)
@Produces(MediaType2.BLACKS_API_BLACK)
public Black updateInvalid(@PathParam("idcontenido") String idcontenido, Black black) {
	//validateUser(stingid);
	validateUpdateBlack(black);
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}

	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(UPDATE_INVALID_QUERY);
		stmt.setString(1, idcontenido);
		int rows = stmt.executeUpdate();
		if (rows == 1)
			black = getBlackFromDatabase(idcontenido);
		else {
			throw new NotFoundException("There's no sting with id_asignatura="
					+ idcontenido);
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

	return black;
}


@GET
@Path("/contenidos")
@Produces(MediaType2.BLACKS_API_BLACK_COLLECTION)
public BlackCollection getContenido(@QueryParam("idasignatura") int idasignatura, @QueryParam("idtipo") int idtipo) {
	BlackCollection blacks = new BlackCollection();
 
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
 
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(GET_BLACK_QUERY_ASIGNATURA);
		stmt.setInt(1, idasignatura );
		stmt.setInt(2, idtipo );
		
		
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			Black black = new Black();
			black.setId_contenido(rs.getString("id_contenido"));
			black.setId_asignatura(rs.getInt("id_asignatura"));
			black.setId_tipo(rs.getInt("id_tipo"));
			black.setTitulo(rs.getString("titulo"));
			black.setDescripcion(rs.getString("descripcion"));
			black.setFecha(rs.getString("fecha"));
			black.setAutor(rs.getString("autor"));
			black.setInvalid(rs.getInt("invalid"));
			blacks.addBlack(black);
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
	return blacks;
}








}
