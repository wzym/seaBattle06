package Model.ArtificialIntelligence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HandledShip {
    private List<VariantToShot> body = new ArrayList<VariantToShot>(4);
    private HashMap<String, VariantToShot> directionsToCheck = new HashMap<String, VariantToShot>();
    private VariantToShot variantToOut;
    private String directToCheck = null;

    public HandledShip(VariantToShot injuredDeck, boolean isThisShipMonoDecking) {
        this.body.add(injuredDeck);
        if (isThisShipMonoDecking) {
            finalizeBuffer();
        } else {
            checkAndSetDirectionsToCheck(injuredDeck.getX(), injuredDeck.getY());
        }
    }

    private void checkAndSetDirectionsToCheck(int x, int y) {
        if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x - 1][y].getCurrentStatus() !=
                VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT
                ) {
            this.directionsToCheck.put("left", ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x - 1][y]);
        }
        if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x + 1][y].getCurrentStatus() !=
                VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT
                ) {
            this.directionsToCheck.put("right", ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x + 1][y]);
        }
        if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x][y - 1].getCurrentStatus() !=
                VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT
                ) {
            this.directionsToCheck.put("top", ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x][y - 1]);
        }
        if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x][y + 1].getCurrentStatus() !=
                VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT
                ) {
            this.directionsToCheck.put("bottom", ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x][y + 1]);
        }
    }

    private void getRandomVariantToCheck() {
        if (!this.directionsToCheck.isEmpty()) {
            int key = (int) (Math.round(Math.random() * (this.directionsToCheck.size() - 1)));
            int counter = 0;
            for (String direct : directionsToCheck.keySet()) {
                if (key == counter++) directToCheck = direct;
            }
            this.variantToOut = this.directionsToCheck.get(this.directToCheck);
        }
    }

    public void setResultOfChecking(VariantToShot.ResultOfShot resultOfShot) {
        switch (resultOfShot) {
            case HIT:
                this.body.add(variantToOut);
                if (this.directToCheck.equals("top")) {
                    this.setVertical();
                    VariantToShot lastTop = this.directionsToCheck.get("top");
                    if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[lastTop.getX()][lastTop.getY() - 1]
                            .getCurrentStatus() != VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT) {
                        this.directionsToCheck.put("top", ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent
                                [lastTop.getX()][lastTop.getY() - 1]);
                    } else {
                        this.directionsToCheck.remove("top");
                    }
                }
                if (this.directToCheck.equals("bottom")) {
                    this.setVertical();
                    VariantToShot lastBottom = this.directionsToCheck.get("bottom");
                    if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[lastBottom.getX()][lastBottom.getY() + 1]
                            .getCurrentStatus() != VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT) {
                        this.directionsToCheck.put("bottom", ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent
                            [lastBottom.getX()][lastBottom.getY() + 1]);
                    } else {
                        this.directionsToCheck.remove("bottom");
                    }
                }
                if (this.directToCheck.equals("left")) {
                    this.setHorizontal();
                    VariantToShot lastTop = this.directionsToCheck.get("left");
                    if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[lastTop.getX() - 1][lastTop.getY()].
                            getCurrentStatus() != VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT) {
                        this.directionsToCheck.put("left", ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent
                                [lastTop.getX() - 1][lastTop.getY()]);
                    } else {
                        this.directionsToCheck.remove("left");
                    }
                }
                if (this.directToCheck.equals("right")) {
                    this.setHorizontal();
                    VariantToShot lastTop = this.directionsToCheck.get("right");
                    if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[lastTop.getX() + 1][lastTop.getY()].
                            getCurrentStatus() != VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT) {
                        this.directionsToCheck.put("right", ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent
                                [lastTop.getX() + 1][lastTop.getY()]);
                    } else {
                        this.directionsToCheck.remove("right");
                    }
                }
                break;
            case DEATH_HIT:
                this.body.add(variantToOut);
                this.finalizeBuffer();
                break;
            case MISS:
                if (this.directToCheck.equals("left")) this.directionsToCheck.remove("left");
                if (this.directToCheck.equals("right")) this.directionsToCheck.remove("right");
                if (this.directToCheck.equals("top")) this.directionsToCheck.remove("top");
                if (this.directToCheck.equals("bottom")) this.directionsToCheck.remove("bottom");
                break;
        }
    }

    public VariantToShot getVariantToOut() {
        this.getRandomVariantToCheck();
        return variantToOut;
    }

    private void setHorizontal() {
        this.directionsToCheck.remove("top");
        this.directionsToCheck.remove("bottom");
    }

    private void setVertical() {
        this.directionsToCheck.remove("left");
        this.directionsToCheck.remove("right");
    }

    public void finalizeBuffer() {
        for (VariantToShot variantToShot : body) {
            int xStart = variantToShot.getX();
            int yStart = variantToShot.getY();
            for (int i = -1; i <= 1; i++) {
                ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[i + xStart][yStart - 1].
                        setCurrentStatus(VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT);
                ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[i + xStart][yStart].
                        setCurrentStatus(VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT);
                ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[i + xStart][yStart + 1].
                        setCurrentStatus(VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT);
            }
        }
    }
}