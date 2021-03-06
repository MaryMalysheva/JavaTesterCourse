package ru.point.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.point.pft.addressbook.model.ContactData;
import ru.point.pft.addressbook.model.Contacts;
import ru.point.pft.addressbook.model.GroupData;
import ru.point.pft.addressbook.model.Groups;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class RemoveContactFromGroup extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("test123"));
      app.contact().returnToHomePage();
    }
    Groups groups = app.db().groups();
    if (app.db().contacts().size() == 0) {
      app.contact().create(new ContactData().
              withFirstName("ФИО").withLastName("ФИО").withMobile("79899999999").withEmail("test@test.test").
              withHomePhone("111").withWorkPhone("333").withAddress("Пермь").
              withEmail2("test2@test.test").withEmail3("test3@test.test").inGroup(groups.iterator().next()));
      app.contact().returnToHomePage();
    }
  }

  @Test
  public void TestDeleteContactFromGroup() {
    Contacts contacts = app.db().contacts();
    Groups groups = app.db().groups();
    if (!contacts.stream().filter((s) -> (s.getGroups().size() > 0)).findAny().isPresent()) {
      app.contact().addContactToGroup(contacts.iterator().next(), groups.iterator().next());
    }
    ContactData deletedFromGroupContact = app.db().contacts().stream().filter((s) -> (s.getGroups().size() > 0)).findAny().get();
    Groups before = deletedFromGroupContact.getGroups();
    GroupData modifiedGroup = before.iterator().next();
    app.contact().deleteContactFromGroup(deletedFromGroupContact, modifiedGroup);
    Groups after = app.db().contacts().stream().filter((s) -> s.equals(deletedFromGroupContact)).findFirst().get().getGroups();
    assertThat(after, equalTo(before.without(modifiedGroup)));
  }
}
