#encoding:utf-8
require 'java'

java_import 'ch.obermuhlner.math.big.BigDecimalMath'
java_import 'java.lang.Math'
java_import 'java.math.BigDecimal'
java_import 'java.math.MathContext'
java_import 'java.math.RoundingMode'
java_import 'java.text.DecimalFormat'

class BigDecimal
  method_hash = {
    :* => :multiply,
    :- => :subtract,
    :+ => :add
  }

  method_hash.each_pair do |bigdecimal_method, bd_method|
    define_method(bigdecimal_method.to_sym) do |other|
      send(bd_method, other)
    end
  end

  def /(other, scale = MathContext::DECIMAL128)
    self.divide(other, scale)
  end

  def %(other, scale = MathContext::DECIMAL128)
    self.remainder(other, scale)
  end

  def acos(scale = MathContext::DECIMAL128)
    BigDecimalMath.acos(self, scale)
  end

  def asin(scale = MathContext::DECIMAL128)
    BigDecimalMath.asin(self, scale)
  end

  def atan(scale = MathContext::DECIMAL128)
    BigDecimalMath.atan(self, scale)
  end

  def atan2(x, scale = MathContext::DECIMAL128)
    BigDecimalMath.atan2(self, x, scale)
    #BigDecimal.valueOf(Math.atan2(self.doubleValue, x.doubleValue)).round(scale)
  end

  def cos(scale = MathContext::DECIMAL128)
    BigDecimalMath.cos(self, scale)
  end

  def sin(scale = MathContext::DECIMAL128)
    BigDecimalMath.sin(self, scale)
  end

  def tan(scale = MathContext::DECIMAL128)
    BigDecimalMath.tan(self, scale)
  end

  def sqrt(scale = MathContext::DECIMAL128)
    BigDecimalMath.sqrt(self, scale)
  end

  def sqrt_fast(scale = MathContext::DECIMAL128)
    x = BigDecimal.new(Math.sqrt(self.doubleValue), MathContext::DECIMAL64)
    return x.round(scale) if scale < 17

    b2 = BigDecimal.new(2)
    temp_scale = 16
    while (temp_scale < scale)
      x = x.subtract(
        x.multiply(x).subtract(self).divide(
          x.multiply(b2), scale, RoundingMode::HALF_EVEN
        )
      )
      temp_scale *= 2
    end
    x.round(scale)
  end

  def round_dec32
    self.round(MathContext::DECIMAL32)
  end

  def round_dec64
    self.round(MathContext::DECIMAL64)
  end

  def round_dec128
    self.round(MathContext::DECIMAL128)
  end

  def set_rounding_mode_up(scale = self.scale)
    self.setScale(scale, RoundingMode::UP)
  end

  def set_rounding_mode_down(scale = self.scale)
    self.setScale(scale, RoundingMode::DOWN)
  end

  def set_rounding_mode_hup(scale = self.scale)
    self.setScale(scale, RoundingMode::HALF_UP)
  end

  def set_rounding_mode_heven(scale = self.scale)
    self.setScale(scale, RoundingMode::HALF_EVEN)
  end

  #弧度法(radian) -> 度数法(degree)
  def radian_to_degree(scale = MathContext::DECIMAL128)
    c = BigDecimal.new("180").divide(BigDecimalMath.pi(scale), scale)
    self * c
  end

  #度数法(degree) -> 弧度法(radian)
  def degree_to_radian(scale = MathContext::DECIMAL128)
    c = BigDecimalMath.pi(scale).divide(BigDecimal.new("180"), scale)
    self * c
  end

  #度数法(degree) -> 度分秒(DMS): 1度(degree)=60分(minute) 1分(minute)=60秒(second)
  def degree_to_dms
    d = self.toPlainString.to_i
    tmp = (self - BigDecimal.new(d.to_s)) * BigDecimal.valueOf(60)
    m = tmp.toPlainString.to_i
    s = DecimalFormat.new("0").
      format((tmp - BigDecimal.new(m.to_s)) * BigDecimal.valueOf(60)).to_i
    "%d.%02d%02d" %[d, m, s]
  end

  #10進数の数値を出力
  def decimal_format(fmt = "0.000", rounding_mode = RoundingMode::HALF_UP)
    df = DecimalFormat.new(fmt)
    df.setRoundingMode(rounding_mode)
    df.format(self)
  end

  #10進数の数値をStringで出力
  def to_p
    self.toPlainString
  end

  def ==(other)
    return case self.compareTo(other)
    when 0
      true
    else
      false
    end
  end

  def !=(other)
    return case self.compareTo(other)
    when 0
      false
    else
      true
    end
  end

  def >(other)
    return case self.compareTo(other)
    when 1
      true
    else
      false
    end
  end

  def >=(other)
    return case self.compareTo(other)
    when 1, 0
      true
    else
      false
    end
  end

  def <(other)
    return case self.compareTo(other)
    when -1
      true
    else
      false
    end
  end

  def <=(other)
    return case self.compareTo(other)
    when 0, -1
      true
    else
      false
    end
  end
end

class String
  #String -> BigDecimal
  def to_d
    java.math.BigDecimal.new(self)
  end

  #度分秒(DMS) -> 度数法(degree): 1度(degree)=60分(minute) 1分(minute)=60秒(second)
  def dms_to_degree
    self.sub(/(\d+)\.(\d\d)(\d\d)/) {
      ($1.to_d + ( $2.to_d / "60".to_d ) + ( $3.to_d / "3600".to_d ) ).to_p
    }.to_d
  end
end

class Array
  #配列の要素を結合して出力
  def to_p
    self.join("\n")
  end
end