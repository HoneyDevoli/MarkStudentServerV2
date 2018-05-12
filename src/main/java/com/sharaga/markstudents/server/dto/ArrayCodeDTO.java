package com.sharaga.markstudents.server.dto;

import com.sharaga.markstudents.server.entity.Code;

import java.util.ArrayList;
import java.util.List;

public class ArrayCodeDTO {

    private List<Code> codes = new ArrayList<>();

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }
}
