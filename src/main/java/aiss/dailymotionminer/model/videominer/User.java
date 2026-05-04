package aiss.dailymotionminer.model.videominer;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*; // Importante para la base de datos

@Entity
@Table(name = "VM_User") // Le cambiamos el nombre a la tabla para que la base de datos no explote
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // La base de datos SIEMPRE necesita un ID (Clave primaria)

    @JsonProperty("name")
    private String name;

    @JsonProperty("user_link")
    private String user_link;

    @JsonProperty("picture_link")
    private String picture_link;

    public User() {}

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUser_link() { return user_link; }
    public void setUser_link(String user_link) { this.user_link = user_link; }

    public String getPicture_link() { return picture_link; }
    public void setPicture_link(String picture_link) { this.picture_link = picture_link; }
}