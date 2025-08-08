package com.ujwal.TrackYourExpense.Repository;

import com.ujwal.TrackYourExpense.Model.userRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepo extends JpaRepository<userRegister, Integer> {
    Optional<userRegister> findByEmailId(String emailId);
}
