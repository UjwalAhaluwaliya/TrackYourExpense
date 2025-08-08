package com.ujwal.TrackYourExpense.Repository;

import com.ujwal.TrackYourExpense.Model.Expense;

import com.ujwal.TrackYourExpense.Model.userRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepo extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(userRegister user);
}
