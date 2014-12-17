package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
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

import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Black;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.BlackCollection;



@Path("/blacks")
public class BlackResource {
	
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	private String GET_BLACK_QUERY_ASIGNATURA = "SELECT * FROM contenidos id_asignatura=? and id_tipo=?";
	//He a�adido esta 
	private String GET_BLACK_QUERY_CONTENIDO = "SELECT * FROM contenidos";
	private String GET_BLACK_QUERY_MATRICULADAS = "select * from contenidos, users_matriculas where username_matriculas=? and id_asignatura_u_matriculas=id_asignatura";
	private String GET_BLACK_QUERY_TITULO = "SELECT * FROM contenidos where titulo=?";
	private String GET_BLACK_QUERY_AUTOR_FROM_LAST = "select c.* from contenidos c where autor LIKE ? and c.fecha > ? order by fecha";
	private String GET_BLACKS_QUERY_AUTOR = "select c.* from contenidos c where autor LIKE ? and c.fecha < ifnull ( ?, now()) order by fecha desc limit ?";
	private String GET_BLACK_QUERY_USUARIO = "SELECT * FROM contenidos where id_usuario=?";
	private String INSERT_BLACK_QUERY = "insert into contenidos (id_asignatura,id_tipo,titulo,descripcion,autor,link) values (?,?,?,?,?,?);";
	//@Context
	//private SecurityContext security;
	
	@GET
	@Produces(MediaType.BLACKS_API_BLACK_COLLECTION)
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
				black.setId_contenido(rs.getInt("id_contenido"));
				black.setId_asignatura(rs.getInt("id_asignatura"));
				black.setId_tipo(rs.getInt("id_tipo"));
				black.setTitulo(rs.getString("titulo"));
				black.setDescripcion(rs.getString("descripcion"));
				//black.setFecha(rs.getFecha("fecha").getTime());
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
	
	
	@POST
	@Consumes(MediaType.BLACKS_API_BLACK)
	@Produces(MediaType.BLACKS_API_BLACK)
	public Black createBlack(Black black) {
		//validateBlack(black);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(INSERT_BLACK_QUERY,
					Statement.RETURN_GENERATED_KEYS);
			//SEGURIDAD!!!
			//stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setInt(1, black.getId_asignatura());
			stmt.setInt(2, black.getId_tipo());
			stmt.setString(3, black.getTitulo());
			stmt.setString(4, black.getDescripcion());
			stmt.setString(5, black.getAutor());
			stmt.setString(6, black.getLink());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int stingid = rs.getInt(1);

				//sting = getStingFromDatabase(Integer.toString(stingid));
			} else {
				// Something has failed...
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
	
	
	
	
	
	
	/*

	@GET
	@Path("/search/{nombreautor}")
	@Produces(MediaType.BLACKS_API_BLACK_COLLECTION)
	public BlackCollection getBlack(
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
				black.setId_contenido(rs.getInt("id_contenido"));
				black.setId_asignatura(rs.getInt("id_asignatura"));
				black.setId_tipo(rs.getInt("id_tipo"));
				black.setTitulo(rs.getString("titulo"));
				black.setDescripcion(rs.getString("descripcion"));
				black.setAutor(rs.getString("autor"));
				black.setLink(rs.getString("link"));
				black.setInvalid(rs.getInt("invalid"));
				black.setFecha(rs.getTimestamp("fecha").getTime());
				oldestTimestamp = rs.getTimestamp("fecha").getTime();
				black.setFecha(oldestTimestamp);
				if (first) {
					first = false;
					coleccionblack.setNewestTimestamp(black.getFecha());
				}
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
	}*/
}