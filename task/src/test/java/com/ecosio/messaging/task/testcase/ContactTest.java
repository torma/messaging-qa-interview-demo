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

    // I would extract all constants from this test to the top but as mentioned in the review.md I'm quite defensive here and only change places where there is a TODO comment.
    List<Contact> contactsWithExistingName = getContactByFirstname("abc");
    // Ideally I would get bach the updated contact object here and could work with that.
    updateContact(contactsBefore.get(0), updatedContact);

    List<Contact> contactsAfter = getContactByFirstname("abc");

    assertThat(contactsAfter.size())
      .as("number of contacts after update")
      .isEqualTo(contactsBefore.size() + contactsWithExistingName.size());
    Contact updatedContactFromResponse = contactsAfter
            .stream()
            .filter(contact -> contact.getId() == updatedContact.getId())
            .findFirst()
            .orElseThrow();
    assertThat(updatedContactFromResponse.getId())
      .as("id after update has changed")
      .isEqualTo(updatedContact.getId());
    assertThat(updatedContactFromResponse.getFirstname())
      .as("firstname after update does not match the expected value")
      .isEqualTo(updatedContact.getFirstname());
    assertThat(updatedContactFromResponse.getLastname())
      .as("lastname after update does not match the expected value")
      .isEqualTo(updatedContact.getLastname());

    // Restore contact
    // Since we don't explicitly control the test data at setup at lest I would restore the original data
    updateContact(updatedContact, contactsBefore.get(0));
  }

  @Test
  void getContactByFirstname() {

    // TODO: get ALL contacts with the string "name" in it and add assertions
    final String firstNameFilter = "name";

    // Before querying the endpoint I would actually add a new contact with firstname containing 'name'

    List<Contact> contactsWithName = List.of();
    try {
      contactsWithName = getContactByFirstname("name");
    } catch (IOException ignored) {
      throw new RuntimeException();
    }

    assertThat(contactsWithName.size())
      .as("Contacts returned with '" + firstNameFilter +"' in their first name should have a positive value")
      .isGreaterThan(0);
    assertThat(contactsWithName)
      .as("All returned contracts should have '" + firstNameFilter + "' in their firstname")
      .allMatch(contact -> contact.getFirstname().contains(firstNameFilter));
  }
}
