package project.ufrn.pw.api_rest.domain;

import jakarta.persistence.*;
import project.ufrn.pw.api_rest.controller.EnderecoController;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@SQLDelete(sql = "UPDATE endereco SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "deleted_at is null")
public class Endereco extends AbstractEntity {
    String rua;

    @Data
    public static class DtoRequest {
        @NotBlank(message = "Rua com nome em branco")
        String rua;

        public static Endereco convertToEntity(DtoRequest dto, ModelMapper mapper) {
            return mapper.map(dto, Endereco.class);
        }
    }

    public void partialUpdate(AbstractEntity e) {}

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class DtoResponse extends RepresentationModel<DtoResponse> {
        String rua;
        
        public static DtoResponse convertToDto(Endereco e, ModelMapper mapper) {
            return mapper.map(e, DtoResponse.class);
        }

        public void generateLinks(Long id){
             add(linkTo(EnderecoController.class).slash(id).withSelfRel());
             add(linkTo(EnderecoController.class).slash(id).withRel("delete"));
             add(linkTo(EnderecoController.class).withRel("usuários"));
        }
    }
}