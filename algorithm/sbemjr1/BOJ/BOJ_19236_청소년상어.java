package sbemjr1.BOJ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class BOJ_19236_청소년상어 {
	static class Point {
		int num, dir;
		boolean isMoved;

		public Point(int num, int dir, boolean isMoved) {
			this.num = num;
			this.dir = dir;
			this.isMoved = isMoved;
		}

		@Override
		public String toString() {
			return "Point [num=" + num + ", dir=" + dir + ", isMoved=" + isMoved + "]";
		}

	}

	static class EatFish {
		int r, c, weight;

		public EatFish(int r, int c, int weight) {
			this.r = r;
			this.c = c;
			this.weight = weight;
		}

		@Override
		public String toString() {
			return "EatFish [r=" + r + ", c=" + c + ", weight=" + weight + "]";
		}

	}

	static int d, lastd, curR, curC;
	static Point[][] map, copy_map;
	// x, ↑, ↖, ←, ↙, ↓, ↘, →, ↗
	static int[] dr = { 0, -1, -1, 0, 1, 1, 1, 0, -1 };
	static int[] dc = { 0, 0, -1, -1, -1, 0, 1, 1, 1 };
//	static LinkedList<Integer> sel;
	static int[] sel;
	static int result, ans;
	static Deque<Point> dq = new ArrayDeque<>();

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		map = new Point[4][4];
		copy_map = new Point[4][4];

		for (int r = 0; r < 4; r++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			for (int c = 0; c < 4; c++) {
				int num = Integer.parseInt(st.nextToken());
				int dir = Integer.parseInt(st.nextToken());
				map[r][c] = new Point(num, dir, false);
				copy_map[r][c] = new Point(0, 0, false);
			}
		}
		// 상어 등장
		map[0][0].num = 99;
		// 상어 이동(조합?)
		sel = new int[3];
		// 현재 상어 위치
		curR = 0;
		curC = 0;
		d = map[curR][curC].dir;
		permutation(curR, curC, 0);
	}

	private static void permutation(int r, int c, int k) {
		if (k == 3) {
			for (int i = 0; i < 3; i++) {
				System.out.print(sel[i] + " ");
			}
			System.out.println();
			return;
		}
		if (map[r][c].num == 0 || r < 0 || r >= 4 || c < 0 || c >= 4) {
			return;
		}

		for (int i = 1; i <= 3; i++) {
			sel[k] = i;

//			for (int l = 0; l < 4; l++) {
//				for (int j = 0; j < 4; j++) {
//					copy_map[l][j].num = map[l][j].num;
//					copy_map[l][j].dir = map[l][j].dir;
//					copy_map[l][j].isMoved = map[l][j].isMoved;
//				}
//			}
			move_fish();
			System.out.println(i);
			move_shark(r, c, i);
			permutation(r, c, k + 1);
//			back_shark(i);
//			System.out.println(Arrays.toString(sel));
//			for (int l = 0; l < 4; l++) {
//				for (int j = 0; j < 4; j++) {
//					map[l][j].num = copy_map[l][j].num;
//					map[l][j].dir = copy_map[l][j].dir;
//					map[l][j].isMoved = copy_map[l][j].isMoved;
//				}
//			}
		}
	}

	private static void back_shark(int k) {
		// 상어가 먹은 물고기 뱉기
		Point owo = dq.pollLast();

		d = lastd;
		// 상어 원래 자리로 돌아가기
		map[curR - dr[d] * k][curC - dc[d] * k].num = map[curR][curC].num;
		map[curR - dr[d] * k][curC - dc[d] * k].dir = map[curR][curC].dir;
		// 그 자리에 물고기 생성
		map[curR][curC].num = owo.num;
		map[curR][curC].dir = owo.dir;
		map[curR][curC].isMoved = owo.isMoved;
	}

	private static void move_shark(int r, int c, int k) {
		// 상어가 먹은 물고기 dq 에서 관리
		dq.offerLast(new Point(map[r + dr[d] * k][c + dc[d] * k].num, map[r + dr[d] * k][c + dc[d] * k].dir,
				map[r + dr[d] * k][c + dc[d] * k].isMoved));
		// 상어 이동 후 원래 위치 비우기
		map[r + dr[d] * k][c + dc[d] * k].num = map[r][c].num;
		map[r + dr[d] * k][c + dc[d] * k].dir = map[r][c].dir;

		map[r][c].num = 0;
		map[r][c].dir = 0;

		// 상어 현재 위치 갱신
		curR = r + dr[d] * k;
		curC = c + dc[d] * k;
		// 상어 방향 갱신
		lastd = d;
		d = map[r + dr[d] * k][c + dc[d] * k].dir;
	}

	private static void move_fish() {
		for (int i = 1; i <= 16; i++) { // 작은 물고기 부터 이동
			for (int r = 0; r < 4; r++) {
				for (int c = 0; c < 4; c++) {
					if (map[r][c].num == i && !map[r][c].isMoved) {
						// 이동하는 칸에 상어가 있거나 범위를 벗어나는 경우 이동할 수 있을 때 까지 반시계 방향으로 회전 시키기
						while (true) {
							int d = map[r][c].dir;
							int nr = r + dr[d];
							int nc = c + dc[d];
							if (nr < 0 || nr >= 4 || nc < 0 || nc >= 4 || map[nr][nc].num == 99) {
								d++;
								if (d == 9) {
									d = 1;
								}
								map[r][c].dir = d;
								continue;
							}
							map[r][c].isMoved = true;
							map[r][c].dir = d;

							int tmp_num = map[nr][nc].num;
							int tmp_dir = map[nr][nc].dir;
							boolean tmp_isMoved = map[nr][nc].isMoved;

							map[nr][nc].num = map[r][c].num;
							map[nr][nc].dir = map[r][c].dir;
							map[nr][nc].isMoved = map[r][c].isMoved;

							map[r][c].num = tmp_num;
							map[r][c].dir = tmp_dir;
							map[r][c].isMoved = tmp_isMoved;
							break;
						}
					}
				}
			}
		}
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				map[r][c].isMoved = false;
			}
		}
	}

}
