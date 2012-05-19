TRANSITIVE_SCOPES = [nil, 'compile']

def transitive_dependencies(*specs)
  specs.flatten.inject([]) do |set, spec| 
    artifact = Buildr.artifact(spec)
    set |= [artifact] unless artifact.type == :pom
    set |= POM.load(artifact.pom).dependencies(TRANSITIVE_SCOPES).map { |spec| Buildr.artifact(spec) }
  end
end

class Buildr::CompileTask
  def with_transitive(*artifacts)
    with transitive_dependencies(*artifacts)
  end
end
  
class Buildr::TestTask
  def with_transitive(*artifacts)
    with transitive_dependencies(*artifacts)
  end
end  

class Buildr::Packaging::Java::WarTask
  def provided_dependencies(*specs)
    self.libs -= Buildr.artifacts(specs)
  end
  
  def runtime_dependencies(*specs)
    self.libs += Buildr.artifacts(specs)
  end
end

class Buildr::ResourcesTask
  def no_filtering
    filter.using { |file, contents| contents }
  end
end