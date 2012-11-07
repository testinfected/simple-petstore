class Buildr::Packaging::Java::WarTask
  def exclude(*specs)
    self.libs -= Buildr.artifacts(specs)
  end
  
  def add(*specs)
    self.libs += Buildr.artifacts(specs)
  end
end

class Buildr::Filter
  def deactivate
    using { |file, contents| contents }
  end
end

class Buildr::Jetty
  def with(options = {})
    props = options[:properties] || {}
    props.each { |name, value| Java.java.lang.System.setProperty(name.to_s, value.to_s) }
  end
end