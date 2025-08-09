package com.ujwal.TrackYourExpense.Service;

import com.ujwal.TrackYourExpense.Model.userRegister;
import com.ujwal.TrackYourExpense.Model.Expense;
import com.ujwal.TrackYourExpense.Repository.UserRepo;
import com.ujwal.TrackYourExpense.Repository.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(userRegister user) {
        if (user.getName() == null || user.getEmailId() == null) {
            return "Name and email are required";
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return "Passwords do not match";
        }
        if (userRepo.findByEmailId(user.getEmailId()).isPresent()) {
            return "Email already registered";
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setConfirmPassword(""); // Clear confirm password after validation

        userRepo.save(user);
        return "Success";
    }

    public Optional<userRegister> findUserByEmail(String email) {
        return userRepo.findByEmailId(email);
    }

    public Expense saveExpense(Expense expense) {
        return expenseRepo.save(expense);
    }

    public List<Expense> getExpensesForUser(userRegister user) {
        return expenseRepo.findByUser(user);
    }

    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepo.findById(id);
    }

    public void deleteExpenseById(Long id) {
        expenseRepo.deleteById(id);
    }
}