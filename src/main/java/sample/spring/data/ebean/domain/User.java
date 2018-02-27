/*
 * Copyright 2008-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.spring.data.ebean.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.ebean.domain.AbstractEntity;
import org.springframework.data.ebean.domain.AggregateRoot;

/**
 * Domain class representing a person emphasizing the use of {@code AbstractEntity}. No declaration of an id is
 * required. The id is typed by the parameterizable superclass.
 *
 * @author Xuegui Yuan
 */
@AggregateRoot
@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
public class User extends AbstractEntity {

  @Embedded
  private FullName fullName;
  private int age;
  private boolean active;

  @Column(nullable = false, unique = true)
  private String emailAddress;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<User> colleagues;

  @ManyToMany
  private Set<Role> roles;

  @ManyToOne
  private User manager;

  @Embedded
  private Address address;

  @Lob
  private byte[] binaryData;

  @ElementCollection
  private Set<String> attributes;

  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  /**
   * Creates a new empty instance of {@code User}.
   */
  public User() {
    this(null, null, null);
  }

  /**
   * Creates a new instance of {@code User} with preinitialized values for firstName, lastName, email address and roles.
   *
   * @param firstName
   * @param lastName
   * @param emailAddress
   * @param roles
   */
  public User(String firstName, String lastName, String emailAddress, Role... roles) {
    this.fullName = new FullName(firstName, lastName);
    this.emailAddress = emailAddress;
    this.active = true;
    this.roles = new HashSet<Role>(Arrays.asList(roles));
    this.colleagues = new HashSet<User>();
    this.attributes = new HashSet<String>();
  }

  public void changeEmail(String emailAddress) {
    this.emailAddress = emailAddress;
    //UserEmailChangedEvent emailChangedEvent = new UserEmailChangedEvent(this);
    //this.registerEvent(emailChangedEvent);
  }

  /**
   * Adds a new colleague to the user. Adding the user himself as colleague is a no-op.
   *
   * @param collegue
   */
  public void addColleague(User collegue) {

    // Prevent from adding the user himself as colleague.
    if (this.equals(collegue)) {
      return;
    }

    colleagues.add(collegue);
    collegue.getColleagues().add(this);
  }

  /**
   * Removes a colleague from the list of colleagues.
   *
   * @param colleague
   */
  public void removeColleague(User colleague) {
    colleagues.remove(colleague);
    colleague.getColleagues().remove(this);
  }
}
