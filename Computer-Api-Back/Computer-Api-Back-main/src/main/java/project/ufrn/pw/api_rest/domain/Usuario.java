package project.ufrn.pw.api_rest.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import project.ufrn.pw.api_rest.controller.UsuarioController;
import org.hibernate.annotations.SQLDelete;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.hibernate.annotations.Where;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import java.util.Collection;
import java.util.Collections;

@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SQLDelete(sql = "UPDATE usuario SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "deleted_at is null")
public class Usuario extends AbstractEntity implements UserDetails{
    String username;
    String login;
    String password;
    Boolean isAdmin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    Endereco meuEndereco;

    @Override
    public void partialUpdate(AbstractEntity e) {
        if (e instanceof Usuario usuario){
            this.username = usuario.username;
            this.login = usuario.login;
            this.password = usuario.password;
        }  
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Data
    public static class DtoRequest {
        @NotBlank(message = "Usuário com nome em branco")
        String username;
        @NotBlank(message = "Login com nome em branco")
        String login;
        @NotBlank(message = "Password com nome em branco")
        String password;
        @NotBlank(message = "Endereço com nome em branco")
        Long endereco_id;

        public static Usuario convertToEntity(DtoRequest dto, ModelMapper mapper) {
            return mapper.map(dto, Usuario.class);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class DtoResponse extends RepresentationModel<DtoResponse> {
        String username;
        String login;
        Boolean isAdmin;
        Long endereco_id;

        public static DtoResponse convertToDto(Usuario u, ModelMapper mapper){
            DtoResponse dto = mapper.map(u, DtoResponse.class);
            dto.setEndereco_id(u.getMeuEndereco().getId());
            return dto;
        }

        public void generateLinks(Long id){
             add(linkTo(UsuarioController.class).slash(id).withSelfRel());
             add(linkTo(UsuarioController.class).withRel("usuários"));
             add(linkTo(UsuarioController.class).slash(id).withRel("delete"));
        }
    }
}