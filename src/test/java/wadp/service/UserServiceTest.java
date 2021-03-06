package wadp.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wadp.Application;
import wadp.domain.Course;
import wadp.domain.CourseProgressTracker;
import wadp.domain.Skill;
import wadp.domain.User;
import wadp.repository.CommentRepository;
import wadp.repository.CourseProgressRepository;
import wadp.repository.CourseRepository;
import wadp.repository.SkillRepository;
import wadp.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseProgressRepository coProgressRepository;

    @Autowired
    UserService service;

    @Autowired
    UserRepository repo;

    private User loggedInUser;

    @Before
    public void setUp() {

        loggedInUser = service.createUser("matti2@meikalainen.com", "salasana", "matti meikalainen", "teacher");
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority(loggedInUser.getUserRole()));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(loggedInUser.getEmail(), loggedInUser.getPassword(), grantedAuths));

    }

    @After
    public void tearDown() {

    }

    private void loginAsAdmin() {
        loggedInUser = service.createUser("mara@meikalainen.com", "salasana", "mara meikalainen", "admin");
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority(loggedInUser.getUserRole()));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(loggedInUser.getEmail(), loggedInUser.getPassword(), grantedAuths));
    }

    @Test
    public void testList() {

        service.createUser("matti@meikalainen.com", "salasana", "matti meikalainen", "teacher");
        List<User> result = service.list();
        assertEquals(result.isEmpty(), false);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCreateUser() {
        service.createUser("jukka@meikalainen.com", "salasana", "jukka meikalainen", "teacher");
        User user = repo.findByEmail("jukka@meikalainen.com");
        String name = "jukka meikalainen";
        assertTrue(user.getName().equals(name));

    }

    @Test
    public void testCreateUserEmailHasCapitalLetters() {
        service.createUser("Jukka@Meikalainen.com", "salasana", "jukka meikalainen", "teacher");
        User user = repo.findByEmail("jukka@meikalainen.com");
        String name = "jukka meikalainen";
        assertTrue(user.getName().equals(name));
    }

    @Test
    public void testCreateUserEmailHasEmptySpace() {
        service.createUser("  Jukka@Salo.com  ", "salasana", "jukka meikalainen", "teacher");
        User user = repo.findByEmail("jukka@salo.com");
        String name = "jukka meikalainen";
        assertTrue(user.getName().equals(name));

    }

    @Test(expected = EmailAlreadyRegisteredException.class)
    public void SameEmailThrowsException() {
        service.createUser("jukka@malo.com", "salasana", "jukka meikalainen", "teacher");
        service.createUser("Jukka@malo.com", "salasana", "jukka meikalainen", "teacher");
    }

    @Test(expected = EmailAlreadyRegisteredException.class)
    public void SameEmailWithCapitals() {
        service.createUser("Jukka@Malo.com", "salasana", "jukka meikalainen", "teacher");
        service.createUser("jukka@malo.com", "salasana", "jukka meikalainen", "teacher");
    }

    @Test(expected = EmailAlreadyRegisteredException.class)
    public void SameEmailWithEmptySpaces() {
        service.createUser("  jukka@Oalo.com", "salasana", "jukka meikalainen", "teacher");
        service.createUser("jukka@oalo.com", "salasana", "jukka meikalainen", "teacher");
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateUserWithNoEmail() {
        service.createUser("", "salasana", "jukka meikalainen", "teacher");
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateUserWithNullEmail() {
        service.createUser(null, "salasana", "jukka meikalainen", "teacher");
    }

    @Test
    public void testFindUserByRole() {
        service.createUser("jusse@meikalainen.com", "salasana", "Yusuf meikalainen", "teacher");
        service.createUser("tartsa@meikalainen.com", "salasana", "Tariq meikalainen", "teacher");
        List<User> teachers = service.findUserByRole("teacher");
        for (User teacher : teachers) {
            assertEquals("teacher", teacher.getUserRole());
        }
    }

    @Test
    public void testAuthenticationSuccess() {
        service.createUser("toni@meikalainen.com", "salasana", "toni meikalainen", "teacher");
        User authenticated = service.authenticate("toni@meikalainen.com", "salasana");

        assertTrue(authenticated.getEmail().equals("toni@meikalainen.com"));

    }

    @Test(expected = AuthenticationException.class)
    public void authenticationFailureWhenUserDoesntExist() {
        service.authenticate("anonymous@anonymous.com", "anonymous");
    }

    @Test(expected = AuthenticationException.class)
    public void authenticateThrowsIfPasswordIsIncorrect() {
        service.createUser("irving@meikalainen.com", "salasana", "irving meikalainen", "teacher");
        service.authenticate("irving@meikalainen.com", "salainensana");
    }

    @Test
    public void getAuthenticateduserSuccess() {

        User user = service.getAuthenticatedUser();
        assertTrue(user.getEmail().equals("matti2@meikalainen.com"));

    }

    @Test
    public void PasswordChangeSuccess() {

        service.ChangePassword("uusisalasana");
        User user = service.getAuthenticatedUser();
        assertTrue(user.passwordEquals("uusisalasana"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotChangetoEmptyPassword() {
        service.ChangePassword("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotChangetoNullPassword() {
        service.ChangePassword(null);
    }

    @Test(expected = AuthenticatedUserIsNullException.class)
    public void CurrentUserDoesntreturn() {

        SecurityContextHolder.clearContext();
        service.ChangePassword("salasana");

    }

    @Test
    public void usersCanChangeTheirSchoolClass() {
        service.changeSchoolClass("12D");
        User user = service.getAuthenticatedUser();
        assertEquals("12D", user.getSchoolClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void schoolClassCannotBeEmpty() {
        service.changeSchoolClass("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void schoolClassCannotBeNull() {
        service.changeSchoolClass(null);
    }

    @Test(expected = AuthenticatedUserIsNullException.class)
    public void CurrentUserDoesntreturnSC() {

        SecurityContextHolder.clearContext();
        service.changeSchoolClass("2D");

    }

    @Test
    public void CanDeleteUsersWithNoCourses() {
        int initUsers = service.list().size();
        service.createUser("keke@meikalainen.com", "salasana", "keke meikalainen", "teacher");
        service.deleteUser(service.findUserByEmail("keke@meikalainen.com").getId());
        assertTrue(service.list().size() == initUsers);
        service.createUser("kalle@meikalainen.com", "salasana", "kalle meikalainen", "student");
        service.deleteUser(service.findUserByEmail("kalle@meikalainen.com").getId());
        assertTrue(service.list().size() == initUsers);
    }

    @Test
    public void DeletingStudentWithCoursesRemovesTracersAndComments() {
        Skill skill = new Skill();
        skillRepository.save(skill);
        Course course = new Course();

        course.setDescription("descr");
        course.setName("name");
        courseRepository.save(course);
        service.createUser("kake@meikalainen.com", "salasana", "kake meikalainen", "student");
        User user = service.findUserByEmail("kake@meikalainen.com");

        CourseProgressTracker tracker = new CourseProgressTracker();
        tracker.setCourse(course);
        tracker.setUser(user);
        coProgressRepository.save(tracker);
        service.deleteUser(service.findUserByEmail("kake@meikalainen.com").getId());
        assertTrue(coProgressRepository.findAll().isEmpty());
        assertTrue(commentRepository.findAll().isEmpty());

    }

    @Test
    public void DeletingTeacherDeletesCoursesTeacherIsAssaignedTo() {
        service.createUser("kolho@meikalainen.com", "salasana", "kolho meikalainen", "teacher");
        User user = service.findUserByEmail("kolho@meikalainen.com");
        Course course = new Course();
        course.setDescription("kuvaus");
        course.setName("nimi");
        course.setTeacher(user);
        courseRepository.save(course);
        assertTrue(courseRepository.findAll().size() == 1);
        service.deleteUser(user.getId());
        assertTrue(courseRepository.findAll().isEmpty());

    }

    @Test
    public void testGetAdmins() {
        loginAsAdmin();
        List<User> admins = service.getAdmins();
        for (User admin : admins) {
            assertEquals("admin", admin.getUserRole());
        }
    }

    @Test
    public void changePasswordAsAdminSuccess() {
        service.createUser("jermu@meikalainen.com", "salasana", "eki meikalainen", "student");
        loginAsAdmin();
        User user = service.changePasswordAsAdmin(repo.findByEmail("jermu@meikalainen.com"), "parempisalasana");

        assertFalse(user.passwordEquals("salasana"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotChangePasswordAsAdminToAEmptyOne() {
        service.createUser("jermu@meikalainen.com", "salasana", "eki meikalainen", "student");
        loginAsAdmin();
        service.changePasswordAsAdmin(repo.findByEmail("jermu@meikalainen.com"), "");
    }

}
