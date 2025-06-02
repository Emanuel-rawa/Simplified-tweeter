package tech.buildrun.springsecurity.controller.dto;

import java.util.List;

public record FeedDto(List<FeedItemDto> feedItemDtos, int page, int pageSize, int totalPages, long totalElemments) {

}
