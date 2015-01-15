
package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api;

import org.apache.commons.codec.digest.DigestUtils;
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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;






import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Asignatura;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Black;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.BlackCollection;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Comentario;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Matricula;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.MatriculaCollection;




@Path("/blacks")
public class BlackResource {
	
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	 //Las querys para la base de datos
	private String GET_BLACK_QUERY_ASIGNATURA = "SELECT * FROM contenidos where id_asignatura=? and id_tipo=?";
	private String GET_BLACK_QUERY = "SELECT * FROM contenidos where id_contenido=?";

	private String GET_BLACK_QUERY_CONTENIDO = "SELECT * FROM contenidos";
	private String GET_BLACK_QUERY_MATRICULADAS = "select * from contenidos, users_matriculas where username_matriculas=? and id_asignatura_u_matriculas=id_asignatura order by fecha desc limit 5";
	private String GET_BLACK_QUERY_TITULO = "SELECT * FROM contenidos where titulo=?";
	
	private String GET_BLACK_QUERY_AUTOR_FROM_LAST = "select c.* from contenidos c where autor LIKE ? and c.fecha > ? order by fecha";
	private String GET_BLACKS_QUERY_AUTOR = "select c.* from contenidos c where autor LIKE ? and c.fecha < ifnull ( ?, now()) order by fecha desc limit ?";
	private String GET_BLACK_QUERY_TITULO_FROM_LAST = "select c.* from contenidos c where titulo LIKE ? and c.fecha > ? order by fecha";
	private String GET_BLACKS_QUERY_TITULO = "select c.* from contenidos c where titulo LIKE ? and c.fecha < ifnull ( ?, now()) order by fecha desc limit ?";
	
	
	
	private String GET_BLACK_QUERY_USUARIO = "SELECT * FROM contenidos where autor=?";
	private String INSERT_BLACK_QUERY = "insert into contenidos (id_contenido,id_asignatura,id_tipo,titulo,descripcion,autor,invalid) values (?,?,?,?,?,?,'0')";
	private String DELETE_BLACK_QUERY = "delete from contenidos where id_contenido=?";
	private String UPDATE_BLACK_QUERY= "update contenidos set  titulo=ifnull(?, titulo), descripcion=ifnull(?, descripcion)) where id_contenido=?";
	private String UPDATE_INVALID_QUERY= "update contenidos set  invalid=invalid+1 where id_contenido=?";
	
	@Context
	private SecurityContext security;
	
	//Funcion que devolvera todos los Blacks que hay en la base de datos

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
	
	
	//Funcion que utilizamos para devolver los blacks de un usuario
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
			stmt = conn.prepareStatement(GET_BLACK_QUERY_USUARIO);
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
	
	//Funcion que retorna los ultimos blakcs en los que esta matriculado un usuario
	private BlackCollection getBlacksultimosFromDatabase(String username) {
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
	
	
// Utiliziamos la funcion "getBlacksFromDatabase" para para pintar los blacks de ese user;
	@GET
	@Path("/{username}")
	@Produces(MediaType2.BLACKS_API_BLACK_COLLECTION)
	public BlackCollection getBlacksUser(@PathParam("username") String username, @Context Request request) {
		BlackCollection blacks = new BlackCollection();
		blacks = getBlacksFromDatabase(username);			 
		return blacks;
	}
	
	
	//Utiliza la funcion "getBlacksultimosFromDatabase" para pintar los ultimos blacks en los que estas matriculado
	@GET
	@Path("ultimos/{username}")
	@Produces(MediaType2.BLACKS_API_BLACK_COLLECTION)
	public BlackCollection getBlacksUser2(@PathParam("username") String username, @Context Request request) {
		BlackCollection blacks = new BlackCollection();
		blacks = getBlacksultimosFromDatabase(username);		
		return blacks;
	}
	
	
	
	
	// pinta por pantalla el contenido de un black por su id utilizando la fucion "getBlackFromDatabase"
	@GET
	@Path("contenido/{idcontenido}")
	@Produces(MediaType2.BLACKS_API_BLACK_COLLECTION)
	public Response getBlacksPorId(@PathParam("idcontenido") String idcontenido, @Context Request request) {
		Black black = new Black();
		CacheControl cc = new CacheControl();
		black = getBlackFromDatabase(idcontenido);		
		String referencia = (black.getFecha());
		EntityTag eTag = new EntityTag(referencia);
		Response.ResponseBuilder rb = request.evaluatePreconditions(eTag); 
		if (rb != null) {
			return rb.cacheControl(cc).tag(eTag).build();		}
		rb = Response.ok(black).cacheControl(cc).tag(eTag);	 
		return rb.build();
	}
	
	
	


	@Context
	private Application app;
	//Sube una nuevo black, este son fotos y el usuario al subir un black tiene que rellenar los siguientes campos:
	// Titulo, tipo de black, id de la asignatura que es, descripcion de black, autor del black, id_contenido sera la foto que subas que mediantes una funcion Crea un id unico y "aleatorio"
	
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
	
	//La funcion que crea mediante el nombre del archivo una id aleatoria y lo convierte a .png
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
				throw new NotFoundException("There's no Black with idcontenido="
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
	
	
	
	
//Funcion que nos permite hacer una busqueda de los blacks que subio un autor concreto. 

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
				black.setInvalid(rs.getInt("invalid"));
				black.setFecha(rs.getString("fecha"));
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
//Funcion que nos permite buscar todos los blacks por su titulo
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
			black.setInvalid(rs.getInt("invalid"));
			black.setFecha(rs.getString("fecha"));
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


//Borra un black por su id
@DELETE
@Path("/{idcontenido}")
public void deleteBlack(@PathParam("idcontenido") String idcontenido) {
	validateUser(idcontenido);
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
			throw new NotFoundException("There's no black with idcontenido="
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

//Funcion para modificar un black (solo el titulo y la descripcion)
@PUT
@Path("/{idcontenido}")
@Consumes(MediaType2.BLACKS_API_BLACK)
@Produces(MediaType2.BLACKS_API_BLACK)
public Black updateBlack(@PathParam("idcontenido") String idcontenido, Black black) {
	validateUser(idcontenido);
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
		stmt.setString(3, idcontenido);

		int rows = stmt.executeUpdate();
		if (rows == 1)
			black = getBlackFromDatabase(idcontenido);
		else {
			throw new NotFoundException("There's no black with idcontenido="
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
	if (black.getTitulo() != null && black.getTitulo().length() > 30)
		throw new BadRequestException(
				"Titulo can't be greater than 30 characters.");
	if (black.getDescripcion() != null && black.getDescripcion().length() > 100)
		throw new BadRequestException(
				"Descripcion can't be greater than 100 characters.");
}

//Funcion que basicaente lo que hace es modificar el valor invalido sumandole 1 para que cuando llege a cierto nivel el administrador sepa que ese contenido hay que revisarlo.
@PUT
@Path("/invalid/{idcontenido}")
@Consumes(MediaType2.BLACKS_API_BLACK)
@Produces(MediaType2.BLACKS_API_BLACK)
public Black updateInvalid(@PathParam("idcontenido") String idcontenido, Black black) {
	//validateUser(idcontenido);
	//validateUpdateBlack(black);
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
			throw new NotFoundException("There's no Black with idcontenido="
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

//Pinta todos los blacks de una asignatura de un tipo concreto (examenes, apuntes, ejercicios)
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


private void validateUser(String idcontenido) {
	Black black = getBlackFromDatabase(idcontenido);
	String username = black.getAutor();
	
	if (!security.getUserPrincipal().getName().equals(username) && !security.getUserPrincipal().getName().equals("admin"))
		throw new ForbiddenException(
				"You are not allowed to modify this Black.");
}







}
