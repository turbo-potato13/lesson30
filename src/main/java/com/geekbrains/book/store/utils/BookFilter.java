package com.geekbrains.book.store.utils;

import com.geekbrains.book.store.entities.Book;
import com.geekbrains.book.store.repositories.specifications.BookSpecifications;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;

@Getter
public class BookFilter {
    private Specification<Book> spec;
    private String filterParams;

    public BookFilter(MultiValueMap<String, String> params) {
        spec = Specification.where(null);
        StringBuilder paramsBuilder = new StringBuilder();
        if (params.containsKey("maxPrice") && !params.get("maxPrice").get(0).isEmpty()) {
            int maxPrice = Integer.parseInt(params.get("maxPrice").get(0));
            spec = spec.and(BookSpecifications.priceLesserOrEqualsThan(maxPrice));
            paramsBuilder.append("&maxPrice=" + maxPrice);
        }
        if (params.containsKey("minPrice") && !params.get("minPrice").get(0).isEmpty()) {
            int minPrice = Integer.parseInt(params.get("minPrice").get(0));
            spec = spec.and(BookSpecifications.priceGreaterOrEqualsThan(minPrice));
            paramsBuilder.append("&minPrice=" + minPrice);
        }
        if (params.containsKey("titlePart") && !params.get("titlePart").get(0).isEmpty()) {
            spec = spec.and(BookSpecifications.titleLike(params.get("titlePart").get(0)));
            paramsBuilder.append("&titlePart=" + params.get("titlePart").get(0));
        }
        if (params.containsKey("genre")) {
            Specification<Book> genreSpec = Specification.where(null);
            // todo разобраться с этим моментом
            for (int i = 0; i < params.get("genre").size(); i++) {
//                if (genreSpec == null) {
//                    genreSpec = BookSpecifications.genreIs(params.get("genre").get(i));
//                } else {
                genreSpec = genreSpec.or(BookSpecifications.genreIs(params.get("genre").get(i)));
//                }
                paramsBuilder.append("&genre=" + params.get("genre").get(i));
            }
            spec = spec.and(genreSpec);
        }
        filterParams = paramsBuilder.toString();
    }
}
