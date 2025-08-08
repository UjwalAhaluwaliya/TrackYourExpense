package com.ujwal.TrackYourExpense.Service;

import com.ujwal.TrackYourExpense.Model.*;
import com.ujwal.TrackYourExpense.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ExpenseRepo expenseRepo;

    // ----- User operations -----
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
        userRepo.save(user);
        return "Success";
    }

    public Optional<userRegister> loginUser(String email, String password) {
        return userRepo.findByEmailId(email)
                .filter(u -> u.getPassword().equals(password));
    }

    public Optional<userRegister> findUserByEmail(String email) {
        return userRepo.findByEmailId(email);
    }

    // ----- Expense operations -----
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
