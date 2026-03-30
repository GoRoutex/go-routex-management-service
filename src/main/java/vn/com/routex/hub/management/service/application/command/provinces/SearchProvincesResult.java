package vn.com.routex.hub.management.service.application.command.provinces;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SearchProvincesResult {
    private List<SearchProvincesItemResult> data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SearchProvincesItemResult {
        private int id;
        private String name;
        private String code;
    }
}
