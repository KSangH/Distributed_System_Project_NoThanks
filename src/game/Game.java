package game;

import java.util.ArrayList;

/*
    Game : 게임에 대한 정보를 관리하는 클래스
 */
public class Game {
    private int[] chips; // 유저별 칩의 개수
    private int[] cards; // 현재 게임의 카드 상황 (값 / 1,2,3,4,5 = user, 0 = 미사용카드, -1 = 제외 카드)
    private int currentcard; // 현재 랜덤으로 선택 된 카드
    private int chip; // PASS 할 때마다 쌓이는 칩의 개수
    private int turn; // 현재 턴
    private boolean end; // 게임 종료 여부
    private int num; // 현재 인원 수

    /*
        생성자
        파라미터 : 인원 수(int)
     */
    public Game(int num) {
        
        // 변수 초기화
        this.num = num;
        end = false;
        chips = new int[5];
        cards = new int[36];
        chip = 0;
        turn = 0;
        
        // 55개의 칩을 num명의 사람에게 분배
        for (int i = 0; i < num; i++)
            chips[i] = 11;

        // 0,1,2번 카드는 미사용 카드
        cards[0] = cards[1] = cards[2] = -1;

        // 9개의 카드를 랜덤으로 제외
        for (int i = 0; i < 9; i++) {
            cards[randCard()] = -1;
        }

        // 현재 보여지는 카드 초기화
        currentcard = randCard();
    }

    /*
        randCard() 함수는 cards 목록에서 사용되지 않은 0의 값을 가진 임의의 카드를 선정하는 함수.
        반환값 : 랜덤으로 선정된 카드목록의 인덱스(int)
     */
    private int randCard() {
        // 1~36 까지의 랜덤값 생성
        int rand = (int) (Math.random() * 36) + 1;

        // key는 카드의 인덱스 값을 위하여 이용,
        // temp 변수는 key가 35번까지 카드를 돌았음에도 rand 값이 변하지 않았으면 게임이 종료.
        int key = 2, temp = rand;
        while (rand != 0) {
            // key의 값 증가
            key = (key + 1) % cards.length;

            // 현재 key가 미사용된 카드이면 rand값을 감소
            if (cards[key] == 0) {
                rand--;
            } else {
                // key의 값이 35이면서, rand의 값이 변하지 않았으면 게임이 종료됬음을 의미.
                if (key == cards.length - 1 && temp == rand) {
                    this.end = true;
                    return 0;
                }
            }
        }
        return key;
    }

    /*
        setNext() 함수는 index번째의 유저가 선택한 code(PASS,RECV)를 받아서 처리하는 함수.
        파라미터 : 해당 유저의 인덱스 값(int), 해당 유저가 선택한 코드(String)
        반환값 : 성공여부(boolean)
     */
    public boolean setNext(int index, String code) {
        // 인덱스 범위를 초과한 경우 에러
        if (index < 0 || index >= num) return false;
        if (turn != index) return false;

        // PASS를 선택한 경우 자신의 칩을 1개 줄어들고, 현재 카드의 칩이 증가.
        if (code.equals("PASS")) {
            chips[index] -= 1;
            chip++;
            // 다음 턴으로 넘김.
            turn = (turn + 1) % num;
        } else {
            // RECV를 선택한 경우, 현재 카드를 자신의 소유로 변경, 현재 칩을 자신의 칩으로 가져옴.
            // 칩의 개수 초기화, 현재 랜덤으로 선택된 카드 초기화
            cards[currentcard] = index + 1;
            chips[index] += chip;
            chip = 0;
            currentcard = randCard();
        }
        return true;
    }

    /*
        isEnd() 함수는 게임이 종료되었는지 여부를 체크하는 함수.
        반환값 : 종료여부(boolean)
     */
    public boolean isEnd() {
        return end;
    }

    /*
        getGameStatus() 함수는 현재 유저의 게임 상태를 문자열로 반환하는 함수.
        파라미터 : 유저의 인덱스(int)
        반환값 : 현재 유저의 게임 상태 정보(유저의 인덱스, 현재턴, 본인의 칩, 현재 카드, 현재 카드의 칩, 각각의 유저의 카드현황)(String)
     */
    public String getGameStatus(int index) {
        return String.join("#", index + "", turn + "",  chip + "", currentcard + "",  chips[index] + "",
                getCards(0), getCards(1), getCards(2), getCards(3), getCards(4));
    }

    /*
        getCards() 함수는 현재 유저의 카드를 문자열로 반환하는 함수.
        파라미터 : 유저의 인덱스(int)
        반환값 : [본인의 소유한 카드리스트] (String)
     */
    private String getCards(int index) {
        String result = "[";
        for (int i = 3; i <= 35; i++) {
            if (cards[i] == index + 1) {
                result += (i + ",");
            }
        }
        result += "]";
        return result;
    }

    /*
        getGameResult() 함수는 게임 결과를 문자열로 반환하는 함수.
        파라미터 : 유저리스트(ArrayList)
        반환값 : 게임결과, 각유저별 1줄씩 (String)
     */
    public String getGameResult(ArrayList<String> userlist){
        String result = "[게임 결과] 점수가 낮은 사람이 승리입니다.\n";
        for(int i=0; i<num; i++){
            int score = 0;
            int before = 0;
            // getCards함수를 통하여 카드목록을 불러와 더함.
            for(String card : getCards(i).replace("[","").replace("]", "").split(",")){
                if(card.equals("")){
                    break;
                }
                int current  = Integer.parseInt(card);
                // 조건 : 연속된 숫자가 아니라면 더함. 연속된 숫자는 가장 작은 값만 더해야 하기 때문.
                if(before + 1 != current){
                    score += current;
                }
                // 이전값을 저장한다.
                before = current;
            }
            // 스코어에서 자신의 칩개수를 뺌.
            score -= chips[i];
            result += userlist.get(i) + " : " + score + "점\n";
        }
        return result;
    }

}
