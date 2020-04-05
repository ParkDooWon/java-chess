package chess;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.board.Board;
import domain.board.BoardDao;
import domain.board.BoardFactory;
import domain.board.InvalidTurnException;
import domain.piece.position.InvalidPositionException;
import domain.piece.team.Team;
import service.WebService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class WebUIChessApplication {
	public static void main(String[] args) {
		staticFiles.location("/public");
		WebService webService = new WebService();
		List<ErrorMessage> errorMessage = new ArrayList<>();

		get("/", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("blackTeamScore", 0);
			model.put("whiteTeamScore", 0);
			model.put("turn", "새 게임 혹은 불러오기를 누르세요!");
			return render(model, "index.html");
		});

		get("/start", (req, res) -> {
			List<String> pieces = webService.showAllPieces();
			Map<String, Object> model = new HashMap<>();
			model.put("pieces", pieces);
			model.put("blackTeamScore", webService.calculateTeamScore(Team.BLACK));
			model.put("whiteTeamScore", webService.calculateTeamScore(Team.WHITE));
			model.put("turn", webService.getTurn());
			model.put("errorMessage", errorMessage);

			return render(model, "index.html");
		});

		post("/move", (req, res) -> {
			Map<String, Object> model = new HashMap<>();

			String source = req.queryParams("source");
			String target = req.queryParams("target");

			try {
				webService.move(source, target);
			} catch (InvalidTurnException | InvalidPositionException e) {
				errorMessage.add(new ErrorMessage(e.getMessage()));
			}

			if (webService.isKingDead()) {
				model.put("winner", webService.findWinner());
				return render(model, "result.html");
			}

			List<String> pieces = webService.showAllPieces();

			model.put("pieces", pieces);
			model.put("blackTeamScore", webService.calculateTeamScore(Team.BLACK));
			model.put("whiteTeamScore", webService.calculateTeamScore(Team.WHITE));
			model.put("turn", webService.getTurn());
			model.put("errorMessage", errorMessage);

			return render(model, "index.html");
		});

        post("/save", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            webService.saveGame();

			List<String> pieces = webService.showAllPieces();

			model.put("pieces", pieces);
			model.put("blackTeamScore", webService.calculateTeamScore(Team.BLACK));
			model.put("whiteTeamScore", webService.calculateTeamScore(Team.WHITE));
			model.put("turn", webService.getTurn());
			model.put("errorMessage", errorMessage);
            return render(model, "index.html");
        });
	}

	private static String render(Map<String, Object> model, String templatePath) {
		return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
	}
}
