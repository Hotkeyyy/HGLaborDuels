package de.hglabor.plugins.ffa.util

import de.hglabor.plugins.ffa.util.PacketByteBuf
import io.netty.buffer.ByteBuf
import kotlin.jvm.JvmOverloads
import java.lang.RuntimeException
import io.netty.buffer.ByteBufAllocator
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import io.netty.util.ByteProcessor
import java.nio.ByteOrder
import kotlin.Throws
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.GatheringByteChannel
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.nio.channels.ScatteringByteChannel
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.experimental.and

class PacketByteBuf(private val parent: ByteBuf) : ByteBuf() {
    fun writeByteArray(bs: ByteArray): PacketByteBuf {
        writeVarInt(bs.size)
        this.writeBytes(bs)
        return this
    }

    /*@JvmOverloads
    fun readByteArray(i: Int = readableBytes()): ByteArray {
        val j = readVarInt()
        return if (j > i) {
            throw DecoderException("ByteArray with size $j is bigger than allowed $i")
        } else {
            val bs = ByteArray(j)
            this.readBytes(bs)
            bs
        }
    }*/

    fun writeIntArray(`is`: IntArray): PacketByteBuf {
        writeVarInt(`is`.size)
        val var3 = `is`.size
        for (var4 in 0 until var3) {
            val i = `is`[var4]
            writeVarInt(i)
        }
        return this
    }

    /*@JvmOverloads
    fun readIntArray(i: Int = readableBytes()): IntArray {
        val j = readVarInt()
        return if (j > i) {
            throw DecoderException("VarIntArray with size $j is bigger than allowed $i")
        } else {
            val `is` = IntArray(j)
            for (k in `is`.indices) {
                `is`[k] = readVarInt()
            }
            `is`
        }
    }*/

    fun writeLongArray(ls: LongArray): PacketByteBuf {
        writeVarInt(ls.size)
        val var3 = ls.size
        for (var4 in 0 until var3) {
            val l = ls[var4]
            writeLong(l)
        }
        return this
    }

    fun writeEnumConstant(enum_: Enum<*>): PacketByteBuf {
        return writeVarInt(enum_.ordinal)
    }

    /*fun readVarInt(): Int {
        var i = 0
        var j = 0
        var b: Int
        do {
            b = readInt()
            i = i or (b and 127 shl j++ * 7)
            if (j > 5) {
                throw RuntimeException("VarInt too big")
            }
        } while (b and 128 == 128)
        return i
    }

    fun readVarLong(): Long {
        var l = 0L
        var i = 0
        var b: Int
        do {
            b = readInt()
            l = l or ((b and 127).toLong() shl i++ * 7)
            if (i > 10) {
                throw RuntimeException("VarLong too big")
            }
        } while (b and 128 == 128)
        return l
    }*/

    fun writeUuid(uUID: UUID): PacketByteBuf {
        writeLong(uUID.mostSignificantBits)
        writeLong(uUID.leastSignificantBits)
        return this
    }

    fun readUuid(): UUID {
        return UUID(readLong(), readLong())
    }

    fun writeVarInt(i: Int): PacketByteBuf {
        var i = i
        while (i and -128 != 0) {
            writeByte(i and 127 or 128)
            i = i ushr 7
        }
        writeByte(i)
        return this
    }

    fun writeVarLong(l: Long): PacketByteBuf {
        var l = l
        while (l and -128L != 0L) {
            writeByte((l and 127L).toInt() or 128)
            l = l ushr 7
        }
        writeByte(l.toInt())
        return this
    }

    /*fun readString(i: Int): String {
        val j = readVarInt()
        return if (j > i * 4) {
            throw DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i * 4 + ")")
        } else if (j < 0) {
            throw DecoderException("The received encoded string buffer length is less than zero! Weird string!")
        } else {
            val string = this.toString(this.readerIndex(), j, StandardCharsets.UTF_8)
            this.readerIndex(this.readerIndex() + j)
            if (string.length > i) {
                throw DecoderException("The received string length is longer than maximum allowed ($j > $i)")
            } else {
                string
            }
        }
    }*/

    @JvmOverloads
    fun writeString(string: String, i: Int = 32767): PacketByteBuf {
        val bs = string.toByteArray(StandardCharsets.UTF_8)
        return if (bs.size > i) {
            throw EncoderException("String too big (was " + bs.size + " bytes encoded, max " + i + ")")
        } else {
            writeVarInt(bs.size)
            this.writeBytes(bs)
            this
        }
    }

    fun readDate(): Date {
        return Date(readLong())
    }

    fun writeDate(date: Date): PacketByteBuf {
        writeLong(date.time)
        return this
    }

    override fun capacity(): Int {
        return parent.capacity()
    }

    override fun capacity(i: Int): ByteBuf {
        return parent.capacity(i)
    }

    override fun maxCapacity(): Int {
        return parent.maxCapacity()
    }

    override fun alloc(): ByteBufAllocator {
        return parent.alloc()
    }

    override fun order(): ByteOrder {
        return parent.order()
    }

    override fun order(byteOrder: ByteOrder): ByteBuf {
        return parent.order(byteOrder)
    }

    override fun unwrap(): ByteBuf {
        return parent.unwrap()
    }

    override fun isDirect(): Boolean {
        return parent.isDirect
    }

    override fun isReadOnly(): Boolean {
        return parent.isReadOnly
    }

    override fun asReadOnly(): ByteBuf {
        return parent.asReadOnly()
    }

    override fun readerIndex(): Int {
        return parent.readerIndex()
    }

    override fun readerIndex(i: Int): ByteBuf {
        return parent.readerIndex(i)
    }

    override fun writerIndex(): Int {
        return parent.writerIndex()
    }

    override fun writerIndex(i: Int): ByteBuf {
        return parent.writerIndex(i)
    }

    override fun setIndex(i: Int, j: Int): ByteBuf {
        return parent.setIndex(i, j)
    }

    override fun readableBytes(): Int {
        return parent.readableBytes()
    }

    override fun writableBytes(): Int {
        return parent.writableBytes()
    }

    override fun maxWritableBytes(): Int {
        return parent.maxWritableBytes()
    }

    override fun isReadable(): Boolean {
        return parent.isReadable
    }

    override fun isReadable(i: Int): Boolean {
        return parent.isReadable(i)
    }

    override fun isWritable(): Boolean {
        return parent.isWritable
    }

    override fun isWritable(i: Int): Boolean {
        return parent.isWritable(i)
    }

    override fun clear(): ByteBuf {
        return parent.clear()
    }

    override fun markReaderIndex(): ByteBuf {
        return parent.markReaderIndex()
    }

    override fun resetReaderIndex(): ByteBuf {
        return parent.resetReaderIndex()
    }

    override fun markWriterIndex(): ByteBuf {
        return parent.markWriterIndex()
    }

    override fun resetWriterIndex(): ByteBuf {
        return parent.resetWriterIndex()
    }

    override fun discardReadBytes(): ByteBuf {
        return parent.discardReadBytes()
    }

    override fun discardSomeReadBytes(): ByteBuf {
        return parent.discardSomeReadBytes()
    }

    override fun ensureWritable(i: Int): ByteBuf {
        return parent.ensureWritable(i)
    }

    override fun ensureWritable(i: Int, bl: Boolean): Int {
        return parent.ensureWritable(i, bl)
    }

    override fun getBoolean(i: Int): Boolean {
        return parent.getBoolean(i)
    }

    override fun getByte(i: Int): Byte {
        return parent.getByte(i)
    }

    override fun getUnsignedByte(i: Int): Short {
        return parent.getUnsignedByte(i)
    }

    override fun getShort(i: Int): Short {
        return parent.getShort(i)
    }

    override fun getShortLE(i: Int): Short {
        return parent.getShortLE(i)
    }

    override fun getUnsignedShort(i: Int): Int {
        return parent.getUnsignedShort(i)
    }

    override fun getUnsignedShortLE(i: Int): Int {
        return parent.getUnsignedShortLE(i)
    }

    override fun getMedium(i: Int): Int {
        return parent.getMedium(i)
    }

    override fun getMediumLE(i: Int): Int {
        return parent.getMediumLE(i)
    }

    override fun getUnsignedMedium(i: Int): Int {
        return parent.getUnsignedMedium(i)
    }

    override fun getUnsignedMediumLE(i: Int): Int {
        return parent.getUnsignedMediumLE(i)
    }

    override fun getInt(i: Int): Int {
        return parent.getInt(i)
    }

    override fun getIntLE(i: Int): Int {
        return parent.getIntLE(i)
    }

    override fun getUnsignedInt(i: Int): Long {
        return parent.getUnsignedInt(i)
    }

    override fun getUnsignedIntLE(i: Int): Long {
        return parent.getUnsignedIntLE(i)
    }

    override fun getLong(i: Int): Long {
        return parent.getLong(i)
    }

    override fun getLongLE(i: Int): Long {
        return parent.getLongLE(i)
    }

    override fun getChar(i: Int): Char {
        return parent.getChar(i)
    }

    override fun getFloat(i: Int): Float {
        return parent.getFloat(i)
    }

    override fun getDouble(i: Int): Double {
        return parent.getDouble(i)
    }

    override fun getBytes(i: Int, byteBuf: ByteBuf): ByteBuf {
        return parent.getBytes(i, byteBuf)
    }

    override fun getBytes(i: Int, byteBuf: ByteBuf, j: Int): ByteBuf {
        return parent.getBytes(i, byteBuf, j)
    }

    override fun getBytes(i: Int, byteBuf: ByteBuf, j: Int, k: Int): ByteBuf {
        return parent.getBytes(i, byteBuf, j, k)
    }

    override fun getBytes(i: Int, bs: ByteArray): ByteBuf {
        return parent.getBytes(i, bs)
    }

    override fun getBytes(i: Int, bs: ByteArray, j: Int, k: Int): ByteBuf {
        return parent.getBytes(i, bs, j, k)
    }

    override fun getBytes(i: Int, byteBuffer: ByteBuffer): ByteBuf {
        return parent.getBytes(i, byteBuffer)
    }

    @Throws(IOException::class)
    override fun getBytes(i: Int, outputStream: OutputStream, j: Int): ByteBuf {
        return parent.getBytes(i, outputStream, j)
    }

    @Throws(IOException::class)
    override fun getBytes(i: Int, gatheringByteChannel: GatheringByteChannel, j: Int): Int {
        return parent.getBytes(i, gatheringByteChannel, j)
    }

    @Throws(IOException::class)
    override fun getBytes(i: Int, fileChannel: FileChannel, l: Long, j: Int): Int {
        return parent.getBytes(i, fileChannel, l, j)
    }

    override fun getCharSequence(i: Int, j: Int, charset: Charset): CharSequence {
        return parent.getCharSequence(i, j, charset)
    }

    override fun setBoolean(i: Int, bl: Boolean): ByteBuf {
        return parent.setBoolean(i, bl)
    }

    override fun setByte(i: Int, j: Int): ByteBuf {
        return parent.setByte(i, j)
    }

    override fun setShort(i: Int, j: Int): ByteBuf {
        return parent.setShort(i, j)
    }

    override fun setShortLE(i: Int, j: Int): ByteBuf {
        return parent.setShortLE(i, j)
    }

    override fun setMedium(i: Int, j: Int): ByteBuf {
        return parent.setMedium(i, j)
    }

    override fun setMediumLE(i: Int, j: Int): ByteBuf {
        return parent.setMediumLE(i, j)
    }

    override fun setInt(i: Int, j: Int): ByteBuf {
        return parent.setInt(i, j)
    }

    override fun setIntLE(i: Int, j: Int): ByteBuf {
        return parent.setIntLE(i, j)
    }

    override fun setLong(i: Int, l: Long): ByteBuf {
        return parent.setLong(i, l)
    }

    override fun setLongLE(i: Int, l: Long): ByteBuf {
        return parent.setLongLE(i, l)
    }

    override fun setChar(i: Int, j: Int): ByteBuf {
        return parent.setChar(i, j)
    }

    override fun setFloat(i: Int, f: Float): ByteBuf {
        return parent.setFloat(i, f)
    }

    override fun setDouble(i: Int, d: Double): ByteBuf {
        return parent.setDouble(i, d)
    }

    override fun setBytes(i: Int, byteBuf: ByteBuf): ByteBuf {
        return parent.setBytes(i, byteBuf)
    }

    override fun setBytes(i: Int, byteBuf: ByteBuf, j: Int): ByteBuf {
        return parent.setBytes(i, byteBuf, j)
    }

    override fun setBytes(i: Int, byteBuf: ByteBuf, j: Int, k: Int): ByteBuf {
        return parent.setBytes(i, byteBuf, j, k)
    }

    override fun setBytes(i: Int, bs: ByteArray): ByteBuf {
        return parent.setBytes(i, bs)
    }

    override fun setBytes(i: Int, bs: ByteArray, j: Int, k: Int): ByteBuf {
        return parent.setBytes(i, bs, j, k)
    }

    override fun setBytes(i: Int, byteBuffer: ByteBuffer): ByteBuf {
        return parent.setBytes(i, byteBuffer)
    }

    @Throws(IOException::class)
    override fun setBytes(i: Int, inputStream: InputStream, j: Int): Int {
        return parent.setBytes(i, inputStream, j)
    }

    @Throws(IOException::class)
    override fun setBytes(i: Int, scatteringByteChannel: ScatteringByteChannel, j: Int): Int {
        return parent.setBytes(i, scatteringByteChannel, j)
    }

    @Throws(IOException::class)
    override fun setBytes(i: Int, fileChannel: FileChannel, l: Long, j: Int): Int {
        return parent.setBytes(i, fileChannel, l, j)
    }

    override fun setZero(i: Int, j: Int): ByteBuf {
        return parent.setZero(i, j)
    }

    override fun setCharSequence(i: Int, charSequence: CharSequence, charset: Charset): Int {
        return parent.setCharSequence(i, charSequence, charset)
    }

    override fun readBoolean(): Boolean {
        return parent.readBoolean()
    }

    override fun readByte(): Byte {
        return parent.readByte()
    }

    override fun readUnsignedByte(): Short {
        return parent.readUnsignedByte()
    }

    override fun readShort(): Short {
        return parent.readShort()
    }

    override fun readShortLE(): Short {
        return parent.readShortLE()
    }

    override fun readUnsignedShort(): Int {
        return parent.readUnsignedShort()
    }

    override fun readUnsignedShortLE(): Int {
        return parent.readUnsignedShortLE()
    }

    override fun readMedium(): Int {
        return parent.readMedium()
    }

    override fun readMediumLE(): Int {
        return parent.readMediumLE()
    }

    override fun readUnsignedMedium(): Int {
        return parent.readUnsignedMedium()
    }

    override fun readUnsignedMediumLE(): Int {
        return parent.readUnsignedMediumLE()
    }

    override fun readInt(): Int {
        return parent.readInt()
    }

    override fun readIntLE(): Int {
        return parent.readIntLE()
    }

    override fun readUnsignedInt(): Long {
        return parent.readUnsignedInt()
    }

    override fun readUnsignedIntLE(): Long {
        return parent.readUnsignedIntLE()
    }

    override fun readLong(): Long {
        return parent.readLong()
    }

    override fun readLongLE(): Long {
        return parent.readLongLE()
    }

    override fun readChar(): Char {
        return parent.readChar()
    }

    override fun readFloat(): Float {
        return parent.readFloat()
    }

    override fun readDouble(): Double {
        return parent.readDouble()
    }

    override fun readBytes(i: Int): ByteBuf {
        return parent.readBytes(i)
    }

    override fun readSlice(i: Int): ByteBuf {
        return parent.readSlice(i)
    }

    override fun readRetainedSlice(i: Int): ByteBuf {
        return parent.readRetainedSlice(i)
    }

    override fun readBytes(byteBuf: ByteBuf): ByteBuf {
        return parent.readBytes(byteBuf)
    }

    override fun readBytes(byteBuf: ByteBuf, i: Int): ByteBuf {
        return parent.readBytes(byteBuf, i)
    }

    override fun readBytes(byteBuf: ByteBuf, i: Int, j: Int): ByteBuf {
        return parent.readBytes(byteBuf, i, j)
    }

    override fun readBytes(bs: ByteArray): ByteBuf {
        return parent.readBytes(bs)
    }

    override fun readBytes(bs: ByteArray, i: Int, j: Int): ByteBuf {
        return parent.readBytes(bs, i, j)
    }

    override fun readBytes(byteBuffer: ByteBuffer): ByteBuf {
        return parent.readBytes(byteBuffer)
    }

    @Throws(IOException::class)
    override fun readBytes(outputStream: OutputStream, i: Int): ByteBuf {
        return parent.readBytes(outputStream, i)
    }

    @Throws(IOException::class)
    override fun readBytes(gatheringByteChannel: GatheringByteChannel, i: Int): Int {
        return parent.readBytes(gatheringByteChannel, i)
    }

    override fun readCharSequence(i: Int, charset: Charset): CharSequence {
        return parent.readCharSequence(i, charset)
    }

    @Throws(IOException::class)
    override fun readBytes(fileChannel: FileChannel, l: Long, i: Int): Int {
        return parent.readBytes(fileChannel, l, i)
    }

    override fun skipBytes(i: Int): ByteBuf {
        return parent.skipBytes(i)
    }

    override fun writeBoolean(bl: Boolean): ByteBuf {
        return parent.writeBoolean(bl)
    }

    override fun writeByte(i: Int): ByteBuf {
        return parent.writeByte(i)
    }

    override fun writeShort(i: Int): ByteBuf {
        return parent.writeShort(i)
    }

    override fun writeShortLE(i: Int): ByteBuf {
        return parent.writeShortLE(i)
    }

    override fun writeMedium(i: Int): ByteBuf {
        return parent.writeMedium(i)
    }

    override fun writeMediumLE(i: Int): ByteBuf {
        return parent.writeMediumLE(i)
    }

    override fun writeInt(i: Int): ByteBuf {
        return parent.writeInt(i)
    }

    override fun writeIntLE(i: Int): ByteBuf {
        return parent.writeIntLE(i)
    }

    override fun writeLong(l: Long): ByteBuf {
        return parent.writeLong(l)
    }

    override fun writeLongLE(l: Long): ByteBuf {
        return parent.writeLongLE(l)
    }

    override fun writeChar(i: Int): ByteBuf {
        return parent.writeChar(i)
    }

    override fun writeFloat(f: Float): ByteBuf {
        return parent.writeFloat(f)
    }

    override fun writeDouble(d: Double): ByteBuf {
        return parent.writeDouble(d)
    }

    override fun writeBytes(byteBuf: ByteBuf): ByteBuf {
        return parent.writeBytes(byteBuf)
    }

    override fun writeBytes(byteBuf: ByteBuf, i: Int): ByteBuf {
        return parent.writeBytes(byteBuf, i)
    }

    override fun writeBytes(byteBuf: ByteBuf, i: Int, j: Int): ByteBuf {
        return parent.writeBytes(byteBuf, i, j)
    }

    override fun writeBytes(bs: ByteArray): ByteBuf {
        return parent.writeBytes(bs)
    }

    override fun writeBytes(bs: ByteArray, i: Int, j: Int): ByteBuf {
        return parent.writeBytes(bs, i, j)
    }

    override fun writeBytes(byteBuffer: ByteBuffer): ByteBuf {
        return parent.writeBytes(byteBuffer)
    }

    @Throws(IOException::class)
    override fun writeBytes(inputStream: InputStream, i: Int): Int {
        return parent.writeBytes(inputStream, i)
    }

    @Throws(IOException::class)
    override fun writeBytes(scatteringByteChannel: ScatteringByteChannel, i: Int): Int {
        return parent.writeBytes(scatteringByteChannel, i)
    }

    @Throws(IOException::class)
    override fun writeBytes(fileChannel: FileChannel, l: Long, i: Int): Int {
        return parent.writeBytes(fileChannel, l, i)
    }

    override fun writeZero(i: Int): ByteBuf {
        return parent.writeZero(i)
    }

    override fun writeCharSequence(charSequence: CharSequence, charset: Charset): Int {
        return parent.writeCharSequence(charSequence, charset)
    }

    override fun indexOf(i: Int, j: Int, b: Byte): Int {
        return parent.indexOf(i, j, b)
    }

    override fun bytesBefore(b: Byte): Int {
        return parent.bytesBefore(b)
    }

    override fun bytesBefore(i: Int, b: Byte): Int {
        return parent.bytesBefore(i, b)
    }

    override fun bytesBefore(i: Int, j: Int, b: Byte): Int {
        return parent.bytesBefore(i, j, b)
    }

    override fun forEachByte(byteProcessor: ByteProcessor): Int {
        return parent.forEachByte(byteProcessor)
    }

    override fun forEachByte(i: Int, j: Int, byteProcessor: ByteProcessor): Int {
        return parent.forEachByte(i, j, byteProcessor)
    }

    override fun forEachByteDesc(byteProcessor: ByteProcessor): Int {
        return parent.forEachByteDesc(byteProcessor)
    }

    override fun forEachByteDesc(i: Int, j: Int, byteProcessor: ByteProcessor): Int {
        return parent.forEachByteDesc(i, j, byteProcessor)
    }

    override fun copy(): ByteBuf {
        return parent.copy()
    }

    override fun copy(i: Int, j: Int): ByteBuf {
        return parent.copy(i, j)
    }

    override fun slice(): ByteBuf {
        return parent.slice()
    }

    override fun retainedSlice(): ByteBuf {
        return parent.retainedSlice()
    }

    override fun slice(i: Int, j: Int): ByteBuf {
        return parent.slice(i, j)
    }

    override fun retainedSlice(i: Int, j: Int): ByteBuf {
        return parent.retainedSlice(i, j)
    }

    override fun duplicate(): ByteBuf {
        return parent.duplicate()
    }

    override fun retainedDuplicate(): ByteBuf {
        return parent.retainedDuplicate()
    }

    override fun nioBufferCount(): Int {
        return parent.nioBufferCount()
    }

    override fun nioBuffer(): ByteBuffer {
        return parent.nioBuffer()
    }

    override fun nioBuffer(i: Int, j: Int): ByteBuffer {
        return parent.nioBuffer(i, j)
    }

    override fun internalNioBuffer(i: Int, j: Int): ByteBuffer {
        return parent.internalNioBuffer(i, j)
    }

    override fun nioBuffers(): Array<ByteBuffer> {
        return parent.nioBuffers()
    }

    override fun nioBuffers(i: Int, j: Int): Array<ByteBuffer> {
        return parent.nioBuffers(i, j)
    }

    override fun hasArray(): Boolean {
        return parent.hasArray()
    }

    override fun array(): ByteArray {
        return parent.array()
    }

    override fun arrayOffset(): Int {
        return parent.arrayOffset()
    }

    override fun hasMemoryAddress(): Boolean {
        return parent.hasMemoryAddress()
    }

    override fun memoryAddress(): Long {
        return parent.memoryAddress()
    }

    override fun toString(charset: Charset): String {
        return parent.toString(charset)
    }

    override fun toString(i: Int, j: Int, charset: Charset): String {
        return parent.toString(i, j, charset)
    }

    override fun hashCode(): Int {
        return parent.hashCode()
    }

    override fun equals(`object`: Any?): Boolean {
        return parent == `object`
    }

    override fun compareTo(byteBuf: ByteBuf): Int {
        return parent.compareTo(byteBuf)
    }

    override fun toString(): String {
        return parent.toString()
    }

    override fun retain(i: Int): ByteBuf {
        return parent.retain(i)
    }

    override fun retain(): ByteBuf {
        return parent.retain()
    }

    override fun touch(): ByteBuf {
        return parent.touch()
    }

    override fun touch(`object`: Any): ByteBuf {
        return parent.touch(`object`)
    }

    override fun refCnt(): Int {
        return parent.refCnt()
    }

    override fun release(): Boolean {
        return parent.release()
    }

    override fun release(i: Int): Boolean {
        return parent.release(i)
    }

    companion object {
        fun getVarIntSizeBytes(i: Int): Int {
            for (j in 1..4) {
                if (i and -1 shl j * 7 == 0) {
                    return j
                }
            }
            return 5
        }
    }
}