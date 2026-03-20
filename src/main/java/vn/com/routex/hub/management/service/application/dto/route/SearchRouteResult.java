package vn.com.routex.hub.management.service.application.dto.route;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SearchRouteResult {
    private List<SearchRouteItemResult> data;
}
