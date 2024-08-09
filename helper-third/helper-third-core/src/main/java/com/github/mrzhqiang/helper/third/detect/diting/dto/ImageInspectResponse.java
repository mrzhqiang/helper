package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageInspectResponse {
    private int code;
    private String msg;
    private List<ImageInspectResponseResults> results;
}
