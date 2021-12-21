package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.JwtToken;
import uz.gvs.admin_crm.payload.ReqLogin;
import uz.gvs.admin_crm.repository.UserRepository;
import uz.gvs.admin_crm.security.CurrentUser;
import uz.gvs.admin_crm.security.JwtTokenProvider;
import uz.gvs.admin_crm.service.AuthService;
import uz.gvs.admin_crm.utils.MessageConst;

import javax.validation.Valid;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    private HttpEntity<?> login(@Valid @RequestBody ReqLogin reqLogin) {
        try {
            Authentication authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(reqLogin.getPhoneNumber(), reqLogin.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(user);
            return ResponseEntity.status(200).body(new ApiResponse(MessageConst.LOGIN_SUCCESS, true, new JwtToken(token)));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse(MessageConst.LOGIN_ERROR, false));
        }
    }
    @GetMapping("/me")
    public HttpEntity<?> getUserMe(@CurrentUser User user) {
        return ResponseEntity.ok(user);
    }
}
