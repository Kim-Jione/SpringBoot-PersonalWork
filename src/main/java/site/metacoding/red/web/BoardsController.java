package site.metacoding.red.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.boards.Boards;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.web.dto.request.boards.WriteDto;

@RequiredArgsConstructor
@Controller
public class BoardsController {

	private final HttpSession session;
	private final BoardsDao boardsDao;

	// @PostMapping("/boards/{id}/delete")
	// @PostMapping("/boards/{id}/update")
	
	@PostMapping("/boards")
	public String writeBoards(WriteDto writeDto) {
		// 1번 세션에 접근해서 세션값을 확인한다. 그때 세션이 Object 타입이기 때문에 Users로 다운캐스팅하고 키값은 principal로
		// 한다
		Users principal = (Users) session.getAttribute("principal");
		
		// 2번 principal이 null인지 확인하고 null이면 loginForm을 리다이렉션 해준다
		if (principal == null) {
			return "redirect:/loginForm";
		}
		
		// 3번 BoardsDao에 접근해서 insert 메서드를 호출한다
		// 조건 : Dto를 entity로 변환해서 인수로 담아준다
		// 조건 : entity에는 세션의 principal에 getId가 필요하다
		boardsDao.insert(writeDto.toEntity(principal.getId()));
		return "redirect:/"; // 메인페이지 이동

		// writeDto.toEntity가 boards가 되서 new 할 필요가 없다
		// Dto에 있는건 그대로 넣으면 되고 UsersId는 세션에서 가져온다
		// 보드스에 인서트 할건데 디티오를 인서트(사용자로부터 받은 값으로)할건데 투엔터티중에 모자란 유저 아이디를 가져온다

	}

	@GetMapping({ "/", "/boards" })
	public String getBoardList() {
		return "boards/main";
	}

	@GetMapping("/boards/{id}")
	public String getBoardList(@PathVariable Integer id) {
		return "boards/detail";
	}

	@GetMapping("/boards/writeForm")
	public String writeForm() {// 글쓰기는 항상 이공식 사용
		System.out.println("1234");
		Users principal = (Users) session.getAttribute("principal");
		if (principal == null) {
			return "redirect:/loginForm";
		}
		return "boards/writeForm";

	}
}