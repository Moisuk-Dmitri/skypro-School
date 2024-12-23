package ru.hogwarts.school.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepositoryMock;

    @InjectMocks
    private FacultyService facultyService;
    Faculty faculty1;

    @BeforeEach
    public void setup() {
        faculty1 = new Faculty("Hogwarts", "red");
    }

    @Test
    @DisplayName("Положительный тест на добавление факультета")
    public void shouldReturnFacultyWhenCreate() {
        when(facultyRepositoryMock.save(faculty1)).thenReturn(faculty1);

        assertEquals(faculty1, facultyService.createFaculty(faculty1));

        verify(facultyRepositoryMock, times(1)).save(faculty1);
    }

    @Test
    @DisplayName("Отрицательный тест на получение факультета")
    public void shouldThrowExceptionWhenRead() {
        assertThrows(WrongIndexException.class, () -> facultyService.readFaculty(1L));
    }

    @Test
    @DisplayName("Положительный тест на редактирование факультета")
    public void shouldReturnFacultyWhenUpdate() {
        when(facultyRepositoryMock.existsById(faculty1.getId())).thenReturn(true);
        when(facultyRepositoryMock.save(faculty1)).thenReturn(faculty1);

        assertEquals(faculty1, facultyService.updateFaculty(faculty1));

        verify(facultyRepositoryMock, times(1)).existsById(faculty1.getId());
        verify(facultyRepositoryMock, times(1)).save(faculty1);
    }

    @Test
    @DisplayName("Отрицательный тест на редактирование факультета")
    public void shouldThrowExceptionWhenUpdate() {
        when(facultyRepositoryMock.existsById(faculty1.getId())).thenThrow(WrongIndexException.class);

        assertThrows(WrongIndexException.class, () -> facultyService.updateFaculty(faculty1));

        verify(facultyRepositoryMock, times(1)).existsById(faculty1.getId());
    }

    @Test
    @DisplayName("Отрицательный тест на удаление факультета")
    public void shouldThrowExceptionWhenDelete() {
        when(facultyRepositoryMock.existsById(1L)).thenThrow(WrongIndexException.class);

        assertThrows(WrongIndexException.class, () -> facultyService.deleteFaculty(1L));

        verify(facultyRepositoryMock, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Положительный тест на фильтрацию факультета по цвету")
    public void shouldReturnFacultyWhenFilterByColor() {
        when(facultyRepositoryMock.findByColorIgnoreCaseOrNameIgnoreCase("red", "")).thenReturn(new HashSet<Faculty>(List.of(faculty1)));

        assertEquals(new HashSet<Faculty>(List.of(faculty1)), facultyService.filterFacultiesByColorOrName("red", ""));

        verify(facultyRepositoryMock, times(1)).findByColorIgnoreCaseOrNameIgnoreCase("red", "");
    }

    @Test
    @DisplayName("Положительный тест на фильтрацию факультета по названию")
    public void shouldReturnFacultyWhenFilterByName() {
        when(facultyRepositoryMock.findByColorIgnoreCaseOrNameIgnoreCase("", "Hogwarts")).thenReturn(new HashSet<Faculty>(List.of(faculty1)));

        assertEquals(new HashSet<Faculty>(List.of(faculty1)), facultyService.filterFacultiesByColorOrName("", "Hogwarts"));

        verify(facultyRepositoryMock, times(1)).findByColorIgnoreCaseOrNameIgnoreCase("", "Hogwarts");
    }

    @Test
    @DisplayName("Положительный тест на получение списка студентов по идентификатору факультета")
    public void shouldReturnStudentsByFacultyId() {
        Student student = new Student();
        faculty1.setStudents(new HashSet<Student>(List.of(student)));

        when(facultyRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(faculty1));

        assertEquals(faculty1.getStudents(), facultyService.getStudentsByFacultyId(1L));

        verify(facultyRepositoryMock, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Положительный тест на получение самого длинного названия факультета")
    public void shouldReturnLongestFacultyName() {
        Faculty faculty2 = new Faculty();
        faculty2.setName("Hog");

        when(facultyRepositoryMock.findAll()).thenReturn(List.of(faculty1, faculty2));

        assertEquals(facultyService.findLongestFacultyName(), faculty1.getName());

        verify(facultyRepositoryMock, times(1)).findAll();
    }
}