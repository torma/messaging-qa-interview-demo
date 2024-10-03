package com.ecosio.messaging.task.testcase;

import com.ecosio.messaging.task.model.Contact;
import com.ecosio.messaging.task.util.HttpClientHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseTest {

  protected final String appUnderTestBaseUrl = "http://localhost:18080/contact/";

  /**
   * gets all contacts where parameter <code>firstname</code> is a substring of the contacts firstname
   *
   * @param firstname
   * @return list of all matching contacts
   * @throws IOException
   */
  protected List<Contact> getContactByFirstname(String firstname) throws IOException {

    HttpGet httpGet = new HttpGet(appUnderTestBaseUrl + "firstname/" + firstname);
    return connectionHelper(httpGet);

  }

  /**
   * gets a list of all contacts stored of the app
   *
   * @return list of all contacts
   * @throws IOException
   */
  protected List<Contact> getAllContacts() throws IOException {

    HttpGet httpGet = new HttpGet(appUnderTestBaseUrl + "allContacts");
    return connectionHelper(httpGet);

  }

  /**
   * updates an existing contact
   *
   * @param currentContact contact to be updated
   * @param updatedContact contact to update the existing one to
   * @throws IOException
   */
  protected void updateContact(
      Contact currentContact,
      Contact updatedContact
  ) {

    // TODO: implement a method updating a contact

  }

  /**
   * connection helper to abstract the "connection layer" from the "application layer"
   *
   * @param httpRequestBase
   * @return list of contacts based on the request
   * @throws IOException
   */
  private List<Contact> connectionHelper(HttpRequestBase httpRequestBase) throws IOException {

    try (CloseableHttpClient httpClient = HttpClientHelper.getHttpClientAcceptsAllSslCerts()) {
      try {

        ObjectMapper mapper = new ObjectMapper();
        String response = IOUtils.toString(
            httpClient.execute(httpRequestBase)
                .getEntity()
                .getContent(),
            StandardCharsets.UTF_8
        );
        List<Contact> contacts = mapper.readValue(
            response,
            new TypeReference<List<Contact>>() {}
        );
        return contacts;

      } finally {
        httpRequestBase.releaseConnection();
      }
    }

  }

}
