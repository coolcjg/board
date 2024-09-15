package com.cjg.traveling.controller;

import com.cjg.traveling.common.response.Response;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.dto.BoardDto;
import com.cjg.traveling.dto.BoardSearchDto;
import com.cjg.traveling.dto.board.BoardListResponseDto;
import com.cjg.traveling.dto.board.DeleteOpinionDto;
import com.cjg.traveling.dto.board.PostBoardRequestDto;
import com.cjg.traveling.dto.board.PutBoardRequestDto;
import com.cjg.traveling.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class BoardController {

	@Autowired
	private Jwt jwt;
	
	@Autowired
	BoardService boardService;
	
	@GetMapping("/board/list")
	@Operation(summary = "게시판 리스트")
	public ResponseEntity<Response<BoardListResponseDto>> list(BoardSearchDto dto){
		return boardService.list(dto);
	}
	
	@PostMapping(value ="/board", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시글 등록", security= @SecurityRequirement(name="accessToken"))
	public ResponseEntity<Response<?>> board(HttpServletRequest request, PostBoardRequestDto postBoardRequestDto, @RequestPart(name = "files", required = false) List<MultipartFile> files) throws Exception{
		String accessToken = request.getHeader("accessToken");
		String userId = jwt.getUserId(accessToken);
		postBoardRequestDto.setUserId(userId);
		return boardService.save(postBoardRequestDto, files);
	}
		
	@GetMapping(value ="/board/{boardId}")
	public Map<String, Object> board(HttpServletRequest request, @PathVariable("boardId") long boardId) throws Exception{
		return boardService.findByBoardId(boardId);
	}
	
	@PutMapping(value ="/board/{boardId}")
	@Operation(summary = "게시글 수정", security= @SecurityRequirement(name="accessToken"))
	public ResponseEntity<Response<String>> updateBoard(HttpServletRequest request, PutBoardRequestDto putBoardRequestDto, @RequestPart(name = "files", required = false) List<MultipartFile> files) throws Exception{
		return boardService.updateBoard(putBoardRequestDto, files);
	}	
	
	@DeleteMapping(value ="/board")
	public Map<String, Object> deleteBoard(HttpServletRequest request, @RequestBody BoardDto boardDTO) throws Exception{
		return boardService.deleteBoard(boardDTO);
	}
	
	@PostMapping(value ="/board/opinion")
	public Map<String, Object> postOpinion(@RequestBody BoardDto boardDTO) throws Exception{
		return boardService.postOpinion(boardDTO);
	}
	
	@DeleteMapping(value ="/board/opinion")
	@Operation(summary = "의견 삭제", security= @SecurityRequirement(name="accessToken"))
	public Map<String, Object> deleteOpinion(@RequestBody DeleteOpinionDto deleteOpinionDto) throws Exception{
		return boardService.deleteOpinion(deleteOpinionDto);
	}
	
	@PostMapping(value ="/board/userOpinion")
	public Map<String, Object> getUserOpinion(@RequestBody BoardDto boardDTO) throws Exception{
		return boardService.getUserOpinion(boardDTO);
	}
}
