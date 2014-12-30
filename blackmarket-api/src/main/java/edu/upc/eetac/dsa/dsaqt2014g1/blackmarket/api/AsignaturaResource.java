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
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Asignatura;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.AsignaturaCollection;

@Path("/asignatura")
public class AsignaturaResource {
	
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	 
	private final static String GET_ASIGNATURAS_QUERY = "select * from asignaturas";
	private final static String GET_ASIGNATURAS_ID_QUERY = "select * from asignaturas where id_asignatura=?";
	private final static String INSERT_ASIGNATURAS_QUERY = "insert into asignaturas (nombre,curso) values (?,?)";
	private final static String DELETE_ASIGNATURAS_QUERY= "delete from asignaturas where id_asignatura=?";
	private final static String UPDATE_ASIGNATURA_QUERY= "update asignaturas set nombre=ifnull(?, nombre), curso=ifnull(?, curso) where id_asignatura=?";
	@GET
	@Produces(MediaType.BLACKS_API_ASIGNATURA_COLLECTION)
	public AsignaturaCollection getAsignatura() {
		AsignaturaCollection asignaturas = new AsignaturaCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_ASIGNATURAS_QUERY);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Asignatura asig = new Asignatura();
				asig.setId_asignatura(rs.getInt("id_asignatura"));
				asig.setNombre(rs.getString("nombre"));
				asig.setCurso(rs.getString("curso"));
				asignaturas.addAsignatura(asig);
				
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
		
		return asignaturas;
	}
	
	private Asignatura getAsignaturaFromDatabase(String idasignatura) {
		Asignatura asignatura = new Asignatura();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_ASIGNATURAS_ID_QUERY);
			stmt.setInt(1, Integer.valueOf(idasignatura));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				asignatura.setId_asignatura(rs.getInt("id_asignatura"));
				asignatura.setNombre(rs.getString("nombre"));
				asignatura.setCurso(rs.getString("curso"));
			} else {
				throw new NotFoundException("There's no sting with stingid="
						+ idasignatura);
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

		return asignatura;
	}
	
	
	@POST
	@Consumes(MediaType.BLACKS_API_ASIGNATURA)
	@Produces(MediaType.BLACKS_API_ASIGNATURA)
	public Asignatura createAsignatura(Asignatura asignatura) {
		validateAsignatura(asignatura);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(INSERT_ASIGNATURAS_QUERY,
					Statement.RETURN_GENERATED_KEYS);

			//stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setString(1, asignatura.getNombre());
			stmt.setString(2, asignatura.getCurso());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int stingid = rs.getInt(1);

				asignatura = getAsignaturaFromDatabase(Integer.toString(stingid));
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

		return asignatura;
	}

	private void validateAsignatura(Asignatura asignatura) {
		if (asignatura.getNombre() == null)
			throw new BadRequestException("Subject can't be null.");
		if (asignatura.getCurso() == null)
			throw new BadRequestException("Content can't be null.");
		
	}
	
	
	@DELETE
	@Path("/{idasignatura}")
	public void deleteAsignatura(@PathParam("idasignatura") String idasignatura) {
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
			stmt = conn.prepareStatement(DELETE_ASIGNATURAS_QUERY);
			stmt.setInt(1, Integer.valueOf(idasignatura));

			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no sting with stingid="
						+ idasignatura);// Deleting inexistent sting
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
	
	
	@PUT
	@Path("/{idasignatura}")
	@Consumes(MediaType.BLACKS_API_ASIGNATURA)
	@Produces(MediaType.BLACKS_API_ASIGNATURA)
	public Asignatura updateAsignatura(@PathParam("idasignatura") String idasignatura, Asignatura asignatura) {
		//validateUser(stingid);
		validateUpdateAsignatura(asignatura);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(UPDATE_ASIGNATURA_QUERY);
			stmt.setString(1, asignatura.getNombre());
			stmt.setString(2, asignatura.getCurso());
			stmt.setInt(3, Integer.valueOf(idasignatura));

			int rows = stmt.executeUpdate();
			if (rows == 1)
				asignatura = getAsignaturaFromDatabase(idasignatura);
			else {
				throw new NotFoundException("There's no sting with id_asignatura="
						+ idasignatura);
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

		return asignatura;
	}
	
	private void validateUpdateAsignatura(Asignatura asignatura) {
		if (asignatura.getNombre() != null && asignatura.getNombre().length() > 20)
			throw new BadRequestException(
					"Nombre can't be greater than 20 characters.");
		if (asignatura.getCurso() != null && asignatura.getCurso().length() > 4)
			throw new BadRequestException(
					"Curso can't be greater than 4 characters.");
	}


}
