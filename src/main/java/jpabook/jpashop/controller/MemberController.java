package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        // result에 error가 있으면 createMemberForm으로 넘겨줌
        if(result.hasErrors()) {
            return "members/createMemberForm";
        }
        
        // Address 만들어줌 (city, street, zipcode 조합)
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        // member 객체에 이름과 주소 세팅
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        // DB에 저장
        memberService.join(member);

        // 페이지로 리다이렉트
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {

        // 모든 멤버를 찾아서 members에 담음
        List<Member> members = memberService.findMembers();

        // members를 model에 담아서 화면에 넘김
        model.addAttribute("members", members);

        return "members/memberList";
    }
}
