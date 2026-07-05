package com.wellness360.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Hypermedia link for REST Level 3 navigation")
public class LinkDto {

    @Schema(description = "URL of the linked resource", example = "/tasks/3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String href;

    @Schema(description = "HTTP method to use", example = "GET")
    private String method;

    public LinkDto() {
    }

    public LinkDto(String href, String method) {
        this.href = href;
        this.method = method;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
