package com.ecosio.messaging.task.testcase;

import com.ecosio.messaging.task.model.Contact;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: have a look at all the existing tests (some may need fixing or can be implemented in a
//  better way, you are not expected to fix them, but think about why and how you would address
//  these issues and we'll talk about them in the next interview)

// TODO: think of a few different tests (including/excluding the ones here)
//  you would implement and why
@Slf4j
public class ContactTest extends BaseTest {

  @Test
  void allContacts() throws IOException {

    // get all available contacts
    List<Contact> contacts = getAllContacts();

    log.info("all contacts: {}", contacts);

    // number of contacts is expected to stay the same
    assertThat(contacts.size())
        .as("number of contacts")
        .isEqualTo(3);

  }

  @Test
  void updateContact() throws IOException {

    // get specific contact
    List<Contact> contactsBefore = getContactByFirstname("Testa");

    assertThat(contactsBefore.size())
        .as("number of contacts before update")
        .isOne();

    // update previously retrieved contact to this
    Contact updatedContact = new Contact(
        contactsBefore.get(0).getId(),
        "abc",
        "def"
    );

    log.info("");

    // TODO: implement remaining testcase

  }

  @Test
  void getContactByFirstname() {

    // TODO: get ALL contacts with the string "name" in it and add assertions

  }

}
