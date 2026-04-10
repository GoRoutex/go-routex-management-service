package vn.com.routex.hub.management.service.interfaces.models.activity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RecentActivitiesFetchResponse extends BaseResponse<RecentActivitiesFetchData> {
}
