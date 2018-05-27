package uk.gov.ons.fwmt.job_service.service;

import uk.gov.ons.fwmt.job_service.data.tm.UserForm;

import java.io.IOException;
import java.util.List;

public interface TMWebDriver {
  void makeNewUser(UserForm userForm) throws IOException;
  void makeNewUsers(List<UserForm> userForms) throws IOException;
}
