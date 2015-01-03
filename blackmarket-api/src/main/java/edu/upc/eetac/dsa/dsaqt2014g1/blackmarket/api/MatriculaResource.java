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

import org.apache.commons.codec.digest.DigestUtils;

import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Asignatura;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.AsignaturaCollection;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Matricula;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.MatriculaCollection;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.User;



@Path("/matricula")
public class MatriculaResource {
	
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	private final static String GET_MATRICULA_QUERY = "select * from users_matriculas";
	private final static String GET_MATRICULA_NOMBRE_QUERY = " select * from users_matriculas, asignaturas where username_matriculas=? and id_asignatura_u_matriculas=id_asignatura";
	private final static String GET_MATRICULA_NOMBRE_ID_QUERY="select * from users_matriculas where username_matriculas=? and id_asignatura_u_matriculas=?";
	private final static String INSERT_MATRICULA_QUERY="insert into users_matriculas (username_matriculas,id_asignatura_u_matriculas) values (?,?)";
	private final static String DELETE_MATRICULA_QUERY= "delete from users_matriculas where username_matriculas=? and id_asignatura_u_matriculas=?";
	
	@GET
	@Produces(MediaType2.BLACKS_API_MATRICULA_COLLECTION)
	public MatriculaCollection getMatricula() {
		MatriculaCollection matriculas = new MatriculaCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_MATRICULA_QUERY);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Matricula matricula = new Matricula();
				matricula.setUsername_matriculas(rs.getString("username_matriculas"));
				matricula.setId_asignatura_u_matriculas(rs.getInt("id_asignatura_u_matriculas"));
				matriculas.addMatricula(matricula);
				
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
		
		return matriculas;
	}
	
	private MatriculaCollection getMatriculasFromDatabase(String username_matricula) {
		MatriculaCollection matriculas = new MatriculaCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_MATRICULA_NOMBRE_QUERY);
			stmt.setString(1, username_matricula);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Matricula matricula = new Matricula();
				//matricula.setUsername_matriculas(rs.getString("username_matriculas"));
				matricula.setId_asignatura_u_matriculas(rs.getInt("id_asignatura_u_matriculas"));
				matriculas.addMatricula(matricula);
				
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

		return matriculas;
	}
	
	
	@GET
	@Path("/{username}")
	@Produces(MediaType2.BLACKS_API_MATRICULA_COLLECTION)
	public MatriculaCollection getMatriculasUser(@PathParam("username") String username, @Context Request request) {
		MatriculaCollection matriculas = new MatriculaCollection();
		//CacheControl cc = new CacheControl();
		matriculas = getMatriculasFromDatabase(username);		
		//String referencia = DigestUtils.md5Hex(matriculas.setUsername_matriculas());
		//EntityTag eTag = new EntityTag(referencia);
		//Response.ResponseBuilder rb = request.evaluatePreconditions(eTag); 
		//if (rb != null) {
			//return rb.cacheControl(cc).tag(eTag).build();
		//}
		//rb = Response.ok(matriculas).cacheControl(cc).tag(eTag);	 
		return matriculas;
	}
	
	
	
	private Matricula getMatriculaFromDatabase(String username, int idmatricula) {
		Matricula matricula = new Matricula();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_MATRICULA_NOMBRE_ID_QUERY);
			stmt.setString(1, username);
			stmt.setInt(2, Integer.valueOf(idmatricula));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				matricula.setUsername_matriculas(rs.getString("username_matriculas"));
				matricula.setId_asignatura_u_matriculas(rs.getInt("id_asignatura_u_matriculas"));
				
			} else {
				throw new NotFoundException("There's no matricula with nombre="
						+ username + "and id"+ idmatricula);
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

		return matricula;
	}
	
	
	@POST
	@Consumes(MediaType2.BLACKS_API_MATRICULA)
	@Produces(MediaType2.BLACKS_API_MATRICULA)
	public  Matricula createMatricula(Matricula matricula) {
		//validateAsignatura(asignatura);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(INSERT_MATRICULA_QUERY,
					Statement.RETURN_GENERATED_KEYS);

			//stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setString(1, matricula.getUsername_matriculas());
			stmt.setInt(2, matricula.getId_asignatura_u_matriculas());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				String username = rs.getString(1);
				int idmatricula = rs.getInt(2);

				matricula = getMatriculaFromDatabase(username, idmatricula);
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

		return matricula;
	}

	
	@DELETE
	@Path("/{username}")
	public void deleteAsignatura(@PathParam("username") String username, @QueryParam("idmatricula") int idmatricula) {
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
			stmt = conn.prepareStatement(DELETE_MATRICULA_QUERY);
			stmt.setString(1,username);
			stmt.setInt(2, Integer.valueOf(idmatricula));

			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no sting with stingid="
						+ username);// Deleting inexistent sting
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
	/* SI HACEMOS QUE EL PUEDA BORRAR
	private void validateUser(String idasignatura) {
		Asignatura asignatura = getStingFromDatabase(idasignatura);
		String username = asignatura.getUsername();
		if (!security.getUserPrincipal().getName().equals(username))
			throw new ForbiddenException(
					"You are not allowed to modify this sting.");
	}*/
	
	

	
	
	

}
