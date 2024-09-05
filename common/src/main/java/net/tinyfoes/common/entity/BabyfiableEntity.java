package net.tinyfoes.common.entity;

public interface BabyfiableEntity {


	default void tinyfoes$$setBabyfied(boolean b) {
	}

	default void tinyfoes$$setBaby(boolean b) {
	}

	default boolean tinyfoes$$isBabyfied() {
		return false;
	}

	default boolean tinyfoes$$isBaby() {
		return false;
	}
}
