package com.bjpowernode.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TClueEditLog implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;
  private Integer clueId;
  private Integer editBy;
  private Date editTime;
  private String changeContent;
  private TUser editByDO;
}
