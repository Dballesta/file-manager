package org.dballesteros.filemanager.domain.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AssetFilterDomain {

    private SortDirectionDomain sortDirection;
}
