package org.africalib.galley.backend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.africalib.galley.backend.entity.Member;
import org.africalib.galley.backend.repository.MemberRepository;
import org.africalib.galley.backend.service.JwtService;
import org.africalib.galley.backend.service.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;


@RestController
public class AccountController {


    @Autowired
    MemberRepository memberRepository;

    @PostMapping("/api/account/login")
    public ResponseEntity login(@RequestBody Map<String, String> params,
                                HttpServletResponse res) {
        //System.out.println("params : "+params.get("email")+"/"+ params.get("password"));
        Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));

        if (member != null) {
            //System.out.println("member : "+ member.getEmail()+"/"+member.getPassword());
            JwtService jwtService = new JwtServiceImpl();

            int id = member.getId();

            String token = jwtService.getToken("id", id);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            res.addCookie(cookie);
            return new ResponseEntity<>(id, HttpStatus.OK);

        }else{
            List<Member> memberList = memberRepository.findAll();
            for (Member member1 : memberList) {
                //System.out.println(member1.getEmail()+" / "+member1.getPassword());

            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


}
