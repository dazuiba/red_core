require "ruble/command"

module Ruble

  class Template < Command
    def initialize(name, path)
      super
    end    
    
    def filetype=(pattern)
      @jobj.filetype = pattern
    end    
    
    def filetype
      @jojb.filetype
    end

    class << self
      def define_template(name, &block)
        log_info("loading template #{name}")

        path = block.binding.eval("__FILE__")
        command = Template.new(name, path)
        block.call(command) if block_given?

        # add command to bundle
        bundle = BundleManager.bundle_from_path(command.path)
        
        if !bundle.nil?
          bundle.add_command(command)
        else
          log_warning("No bundle found for template #{name}: #{command.path}")
        end
      end
    end

    private

    def create_java_object
      com.aptana.scripting.model.TemplateElement.new(path)
    end
  end

end

# define top-level convenience methods

def template(name, &block)
  Ruble::Template.define_template(name, &block)
end
