package com.github.funczz.kotlin.undo

import java.util.ArrayDeque
import java.util.Optional
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * @author funczz
 */
@Suppress("Unused", "MemberVisibilityCanBePrivate")
open class UndoManager<T : Any> {

    /**
     * デフォルトコンストラクタ
     * 初期値、UNDO/REDOアイテム格納数の初期サイズと最大サイズを指定する
     */
    constructor(
        initialItem: T,
        numElements: Int = INITIAL_NUM_ELEMENTS,
        maxElements: Optional<Int> = Optional.empty(),
    ) {
        currentItem = initialItem
        undoDeque = ArrayDeque(numElements)
        redoDeque = ArrayDeque(numElements)
        maxDequeSize = maxElements
    }

    /**
     * UNDO/REDOアイテム格納数の初期サイズを指定する
     */
    constructor(
        initialItem: T,
        numElements: Int,
    ) : this(
        initialItem = initialItem,
        numElements = numElements,
        maxElements = Optional.empty(),
    )

    /**
     * UNDO/REDOアイテム格納数の最大サイズを指定する
     */
    constructor(
        initialItem: T,
        maxElements: Optional<Int>,
    ) : this(
        initialItem = initialItem,
        numElements = INITIAL_NUM_ELEMENTS,
        maxElements = maxElements,
    )

    /**
     * UNDO/REDOアイテムコレクション、UNDO/REDOアイテム格納数の初期サイズと最大サイズを指定する
     */
    constructor(
        initialItem: T,
        undoItems: Collection<T>,
        redoItems: Collection<T>,
        numElements: Int = INITIAL_NUM_ELEMENTS,
        maxElements: Optional<Int> = Optional.empty(),
    ) : this(
        initialItem = initialItem,
        numElements = numElements,
        maxElements = maxElements,
    ) {
        undoItems.reversed()
            .forEach { pushDeque(item = it, deque = undoDeque, maxSize = maxDequeSize) }
        redoItems.reversed()
            .forEach { pushDeque(item = it, deque = redoDeque, maxSize = maxDequeSize) }
    }

    /**
     * 現在のアイテム
     */
    var currentItem: T
        private set

    /**
     * UNDOアイテムを格納する配列
     */
    private val undoDeque: ArrayDeque<T>

    /**
     * REDOアイテムを格納する配列
     */
    private val redoDeque: ArrayDeque<T>

    /**
     * 格納するアイテムの最大サイズ
     */
    private val maxDequeSize: Optional<Int>

    /**
     * 排他制御に用いるロック
     */
    private val lock = ReentrantLock()

    /**
     * UNDOアイテムコレクションにアイテムを追加する
     * 既存のREDOアイテムコレクションは無効となる
     * @param item
     */
    open fun change(item: T) = lock.withLock {
        pushDeque(item = currentItem, deque = undoDeque, maxSize = maxDequeSize)
        currentItem = item
        redoDeque.clear()
    }

    /**
     * UNDO/REDOアイテムコレクションからアイテムを消去する
     */
    open fun clear() = lock.withLock {
        undoDeque.clear()
        redoDeque.clear()
    }

    /**
     * UNDO処理を行う
     * 対象のアイテムをREDOアイテムコレクションに移す
     * @return UNDOアイテムを返す.
     *         アイテムがないならUndoFailedExceptionをスローする
     * @throws UndoFailedException
     */
    open fun undo(): T = lock.withLock {
        if (undoDeque.isEmpty()) throw UndoFailedException("There is no item.")
        pushDeque(item = currentItem, deque = redoDeque, maxSize = maxDequeSize)
        undoDeque.removeFirst().also {
            currentItem = it
        }
    }

    /**
     * REDO処理を行う
     * 対象のアイテムをUNDOアイテムコレクションに移す
     * @return REDOアイテムを返す.
     *         アイテムがないならRedoFailedExceptionをスローする
     * @throws RedoFailedException
     */
    open fun redo(): T = lock.withLock {
        if (redoDeque.isEmpty()) throw RedoFailedException("There is no item.")
        pushDeque(item = currentItem, deque = undoDeque, maxSize = maxDequeSize)
        redoDeque.removeFirst().also {
            currentItem = it
        }
    }

    /**
     * UNDOアイテムの有無を判定する
     * @return UNDOアイテムがあるなら真を返す
     */
    open fun canUndo(): Boolean = lock.withLock {
        undoDeque.isNotEmpty()
    }

    /**
     * REDOアイテムの有無を判定する
     * @return REDOアイテムがあるなら真を返す
     */
    open fun canRedo(): Boolean = lock.withLock {
        redoDeque.isNotEmpty()
    }

    /**
     * @return UNDOアイテムコレクションを返す
     */
    open fun undoItems(): Sequence<T> = lock.withLock {
        return undoDeque.asSequence()
    }

    /**
     * @return REDOアイテムコレクションを返す
     */
    open fun redoItems(): Sequence<T> = lock.withLock {
        return redoDeque.asSequence()
    }

    /**
     * デキューにアイテムを追加する
     * デキューの最大サイズが指定されているなら、デキューのサイズを調整してからアイテムを追加する
     * @param item 追加するアイテム
     * @param deque アイテムを格納するデキュー
     * @param maxSize デキューの最大サイズ
     * @return 追加したアイテム
     */
    private fun pushDeque(item: T, deque: ArrayDeque<T>, maxSize: Optional<Int>): T {
        if (maxSize.isPresent) {
            val i = maxSize.get()
            while (deque.size >= i) {
                deque.removeLast()
            }
        }
        deque.push(item)
        return item
    }

    companion object {
        /**
         * UNDO/REDOアイテム数の初期サイズ
         */
        const val INITIAL_NUM_ELEMENTS = 16
    }

    /**
     * 例外エラーの抽象クラス
     * @author funczz
     */
    abstract class FailedException(message: String) : Exception(message) {
        companion object {
            private const val serialVersionUID: Long = -1296658642330455985L
        }
    }

    /**
     * UNDO処理時の例外エラー
     * @author funczz
     */
    class UndoFailedException(message: String) : FailedException(message) {
        companion object {
            private const val serialVersionUID: Long = 4917400106765908083L
        }
    }

    /**
     * REDO処理時の例外エラー
     * @author funczz
     */
    class RedoFailedException(message: String) : FailedException(message) {
        companion object {
            private const val serialVersionUID: Long = -1879448524464705632L
        }
    }

}
