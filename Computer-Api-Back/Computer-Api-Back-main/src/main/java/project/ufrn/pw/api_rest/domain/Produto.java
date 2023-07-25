package project.ufrn.pw.api_rest.domain;

import org.modelmapper.ModelMapper;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import project.ufrn.pw.api_rest.controller.ProdutoController;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE produto SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "deleted_at is null")
public class Produto extends AbstractEntity{
    String nome_produto;
    Float preco;
    String descricao;

    @Data
    public static class DtoRequest {
        @NotBlank(message = "Produto com nome em branco")
        String nome_produto;
        @NotBlank(message = "Preço com nome em branco")
        String preco;
        @NotBlank(message = "Descrição com nome em branco")
        String descricao;

        public static Produto convertToEntity(DtoRequest dto, ModelMapper mapper) {
            return mapper.map(dto, Produto.class);
        }
    }

    @Override
    public void partialUpdate(AbstractEntity e) {}

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class DtoResponse extends RepresentationModel<DtoResponse>{
        String nome_produto;
        String preco;
        String descricao;

        public static DtoResponse convertToDto(Produto p, ModelMapper mapper) {
            return mapper.map(p, DtoResponse.class);
        }

         public void generateLinks(Long id){
             add(linkTo(ProdutoController.class).slash(id).withSelfRel());
             add(linkTo(ProdutoController.class).withRel("usuários"));
             add(linkTo(ProdutoController.class).slash(id).withRel("delete"));
        }
    }
}