package com.ecosio.qa.gitlabinterface.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactDto {

  private Contact contact;
  private String errorMessage;

}
