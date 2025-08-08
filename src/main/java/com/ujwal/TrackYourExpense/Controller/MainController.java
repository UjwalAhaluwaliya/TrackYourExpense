package com.ujwal.TrackYourExpense.Controller;

import com.ujwal.TrackYourExpense.Model.*;

import com.ujwal.TrackYourExpense.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UserService appService;

    // --- Home ---
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // --- Register ---
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userRegister", new userRegister());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute userRegister userRegister, Model model) {
        String res = appService.registerUser(userRegister);
        if ("Success".equals(res)) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", res);
            return "register";
        }
    }

    // --- Login / Logout ---
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession session, Model model) {
        Optional<userRegister> opt = appService.loginUser(email, password);
        if (opt.isPresent()) {
            session.setAttribute("loggedUserEmail", email);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // helper to get logged user
    private userRegister getCurrentUser(HttpSession session) {
        String email = (String) session.getAttribute("loggedUserEmail");
        if (email == null) return null;
        return appService.findUserByEmail(email).orElse(null);
    }

    // --- Dashboard (list expenses) ---
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        userRegister user = getCurrentUser(session);
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("expenses", appService.getExpensesForUser(user));
        return "dashboard";
    }

    // --- Add Expense ---
    @GetMapping("/expenses/new")
    public String showAddExpense(Model model, HttpSession session) {
        userRegister user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        Expense e = new Expense();
        e.setDate(LocalDate.now());
        model.addAttribute("expense", e);
        return "addExpense";
    }

    @PostMapping("/expenses")
    public String addExpense(@ModelAttribute Expense expense, HttpSession session) {
        userRegister user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        expense.setUser(user);
        appService.saveExpense(expense);
        return "redirect:/dashboard";
    }

    // --- Edit Expense ---
    @GetMapping("/expenses/edit/{id}")
    public String showEditExpense(@PathVariable Long id, Model model, HttpSession session) {
        userRegister user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        Optional<Expense> opt = appService.getExpenseById(id);
        if (opt.isEmpty()) return "redirect:/dashboard";

        Expense expense = opt.get();
        // ownership check
        if (!expense.getUser().getEmailId().equals(user.getEmailId())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("expense", expense);
        return "editExpense";
    }

    @PostMapping("/expenses/update")
    public String updateExpense(@ModelAttribute Expense expense, HttpSession session) {
        userRegister user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        Optional<Expense> opt = appService.getExpenseById(expense.getId());
        if (opt.isEmpty()) return "redirect:/dashboard";

        Expense existing = opt.get();
        if (!existing.getUser().getEmailId().equals(user.getEmailId())) {
            return "redirect:/dashboard";
        }

        expense.setUser(user);
        appService.saveExpense(expense);
        return "redirect:/dashboard";
    }

    // --- Delete Expense ---
    @GetMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable Long id, HttpSession session) {
        userRegister user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        Optional<Expense> opt = appService.getExpenseById(id);
        if (opt.isPresent() && opt.get().getUser().getEmailId().equals(user.getEmailId())) {
            appService.deleteExpenseById(id);
        }
        return "redirect:/dashboard";
    }
}
