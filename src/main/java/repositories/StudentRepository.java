package repositories;

import entities.Student;
import repositories.base.Repository;

import java.util.Optional;

public interface StudentRepository extends Repository<Student, Integer> {


    Optional<Student> findById(int id);


    @Override
    default Optional<Student> findById(Integer id) {
        return findById(id.intValue());
    }
}
