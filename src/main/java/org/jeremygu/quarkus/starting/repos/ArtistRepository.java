package org.jeremygu.quarkus.starting.repos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.jboss.logging.Logger;
import org.jeremygu.quarkus.starting.models.Artist;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ArtistRepository implements GenericRepository<Artist, Long> {

  @Inject
  DataSource dataSource;

  @Inject
  private Logger logger;

 @Override
  public List<Artist> findAll() throws SQLException {
      List<Artist> artists = new ArrayList<>();
      var sql = "SELECT id, name, bio, created_date FROM t_artists";

      try (var conn = dataSource.getConnection();
          var stmt = conn.prepareStatement(sql);
          var result = stmt.executeQuery()) {

          while (result.next()) {
              var id = result.getLong("id");
              var name = result.getString("name");
              var bio = result.getString("bio");
              var createdDate = result.getTimestamp("created_date").toInstant(); // Assuming `created_date` is of type TIMESTAMP in your DB

              Artist artist = new Artist(id, name, bio, createdDate);
              artists.add(artist);
          }
      } catch (SQLException e) {
          // Log and handle exception as appropriate
          logger.error("Error retrieving artists from database", e);
      }

      return artists;
  }


  @Override
  public Optional<Artist> findById(Long id) {
      var sql = "SELECT id, name, bio, created_date FROM t_artists WHERE id = ?";

      try (var conn = dataSource.getConnection();
          var stmt = conn.prepareStatement(sql)) {

          stmt.setLong(1, id);
          var result = stmt.executeQuery();

          if (result.next()) { // Check if the result set has any entry
              var name = result.getString("name");
              var bio = result.getString("bio");
              var createdDate = result.getTimestamp("created_date").toInstant(); // Convert TIMESTAMP to Instant

              Artist artist = new Artist(id, name, bio, createdDate);
              return Optional.of(artist); // Return an Optional containing the artist
          }
      } catch (SQLException e) {
          // Log and handle exception as appropriate
          logger.error("Error retrieving artist from database", e);
      }
      return Optional.empty(); // Return an empty Optional if the artist is not found or an exception occurs
  }


  @Override
  public Artist save(Artist entity) {
    if (entity.getId() == null) {
        // Insert new artist
        var sqlInsert = "INSERT INTO t_artists (name, bio) VALUES (?, ?) RETURNING id";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sqlInsert)) {

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getBio());

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    entity.setId(id); // Set the generated id back to the entity
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving new artist to database", e);
            throw new RuntimeException("Error saving new artist to database", e);
        }
    } else {
        // Update existing artist
        var sqlUpdate = "UPDATE t_artists SET name = ?, bio = ?, created_date = ? WHERE id = ?";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sqlUpdate)) {

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getBio());
            stmt.setTimestamp(3, Timestamp.from(entity.getCreatedDate()));
            stmt.setLong(4, entity.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating artist failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.error("Error updating artist in database", e);
            throw new RuntimeException("Error updating artist in database", e);
        }
    }

    return entity; // Return the entity, now with an ID if it was an insert operation
}


  @Override
  public void delete(Artist entity) {
    var sql = "DELETE FROM t_artists WHERE id = ?";

      try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {

        stmt.setLong(1, entity.getId()); // Set the id in the prepared statement
        int affectedRows = stmt.executeUpdate(); // Execute the delete operation

        if (affectedRows == 0) {
            // Optional: handle the case where no rows were deleted (e.g., the artist was not found)
            logger.info("No artist found with id " + entity.getId() + ", nothing was deleted.");
        }
      } catch (SQLException e) {
          // Log and handle the exception as appropriate
          logger.error("Error deleting artist from database", e);
      }
  }

  @Override
  public long count() {
    // Correct the SQL query to be a complete SELECT statement
    var sql = "SELECT COUNT(id) FROM t_artists";
    long count = 0; // Initialize count

    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql);
         var result = stmt.executeQuery()) {

        // Move to the first row of the result set (there should only be one row in this case)
        if (result.next()) {
            // Retrieve the count from the first column of the result set
            count = result.getLong(1); // Count is in the first column
        }
    } catch (SQLException e) {
        // Log and handle the exception as appropriate
        logger.error("Error retrieving artist count from database", e);
        // Optionally, rethrow as a unchecked exception or handle it according to your error handling policy
        throw new RuntimeException("Error retrieving artist count from database", e);
    }
    return count;
}

}
