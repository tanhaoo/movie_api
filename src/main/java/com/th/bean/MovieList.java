package com.th.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author TanHaooo
 * @date 2021/4/7 18:58
 */
@Data
public class MovieList implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String listName;

    private Integer movieId;

}
