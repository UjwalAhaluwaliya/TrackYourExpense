package com.ujwal.TrackYourExpense.Controller;

import com.ujwal.TrackYourExpense.Model.userRegister;
import com.ujwal.TrackYourExpense.Model.Expense;
import com.ujwal.TrackYourExpense.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UserService appService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

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

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        Optional<userRegister> userOpt = appService.findUserByEmail(email);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        userRegister user = userOpt.get();
        model.addAttribute("user", user);
        model.addAttribute("expenses", appService.getExpensesForUser(user));
        return "dashboard";
    }

    @GetMapping("/expenses/new")
    public String showAddExpense(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";

        Expense expense = new Expense();
        expense.setDate(LocalDate.now());
        model.addAttribute("expense", expense);
        return "addExpense";
    }

    @PostMapping("/expenses")
    public String addExpense(@ModelAttribute Expense expense, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";

        String email = userDetails.getUsername();
        Optional<userRegister> userOpt = appService.findUserByEmail(email);
        if (userOpt.isEmpty()) return "redirect:/login";

        expense.setUser(userOpt.get());
        appService.saveExpense(expense);
        return "redirect:/dashboard";
    }

    @GetMapping("/expenses/edit/{id}")
    public String showEditExpense(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";

        Optional<Expense> opt = appService.getExpenseById(id);
        if (opt.isEmpty()) return "redirect:/dashboard";

        Expense expense = opt.get();
        if (!expense.getUser().getEmailId().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("expense", expense);
        return "editExpense";
    }

    @PostMapping("/expenses/update")
    public String updateExpense(@ModelAttribute Expense expense, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";

        Optional<Expense> opt = appService.getExpenseById(expense.getId());
        if (opt.isEmpty()) return "redirect:/dashboard";

        Expense existing = opt.get();
        if (!existing.getUser().getEmailId().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        expense.setUser(existing.getUser());
        appService.saveExpense(expense);
        return "redirect:/dashboard";
    }

    @GetMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";

        Optional<Expense> opt = appService.getExpenseById(id);
        if (opt.isPresent() && opt.get().getUser().getEmailId().equals(userDetails.getUsername())) {
            appService.deleteExpenseById(id);
        }
        return "redirect:/dashboard";
    }
}