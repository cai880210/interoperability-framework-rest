/*
    
    Copyright 2011 The IMPACT Project
    
    @author Dennis
    @version 0.1

    Licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

*/

package eu.digitisation.dp.rest.model.workflows;

import uk.org.taverna.server.client.AbstractPortValue;
import uk.org.taverna.server.client.OutputPort;
import uk.org.taverna.server.client.PortListValue;
import uk.org.taverna.server.client.Run;

import java.util.ArrayList;
import java.util.List;


/**
 * Bundles several Taverna workflow outputs
 *
 * @author dennis
 *
 */

public class TavernaWorkflowOutputPortBean {

    private String name;
    private List<TavernaWorkflowOutputBean> outputs;

    public TavernaWorkflowOutputPortBean(String name) {
        this.name = name;
        this.outputs = new ArrayList<TavernaWorkflowOutputBean>();
    }

    public TavernaWorkflowOutputPortBean()
    {
        this.outputs = new ArrayList<TavernaWorkflowOutputBean>();
    }

    public TavernaWorkflowOutputPortBean(List<List<TavernaWorkflowOutputBean>> workflowOutput) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TavernaWorkflowOutputBean> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TavernaWorkflowOutputBean> outputs) {
        this.outputs = outputs;
    }

    public void setOutput(TavernaWorkflowOutputBean output) {
        this.outputs.add(output);
    }

    public void setOutput(String name, String url, String output) {
        if (output == null) return;
        TavernaWorkflowOutputBean translate = new TavernaWorkflowOutputBean();

        this.name = name;
        translate.setUrl(url);
        translate.setValue(output);
        this.outputs.add(translate);
    }

    public void setOutput(OutputPort output,boolean binary) {
        TavernaWorkflowOutputBean translate = new TavernaWorkflowOutputBean();
        this.name = output.getName();

        if (!output.getContentType().equals("application/x-list")) {
            String outputAsString = output.getDataAsString();
            String data = outputAsString;
            translate.setValue(data);
            translate.setUrl(output.getValue().toString());
            translate.setBinary(binary);
        } else {
            String data = "";
            // As suggested by Robert Haines on the taverna-hackers mailinglist.
            for (AbstractPortValue p: output.getValue()) data = data + p.getDataAsString();

            translate.setValue(data.toString());
            translate.setUrl(data.toString());
            translate.setBinary(binary);
        }
        if (outputs == null) outputs = new ArrayList<TavernaWorkflowOutputBean>();
        this.outputs.add(translate);
    }

    public void setOutput(OutputPort output, Run run, String name, int depth) {
        TavernaWorkflowOutputBean translate = new TavernaWorkflowOutputBean();
        
        PortListValue listPorts = (PortListValue) output.getValue();
        
        for (List<AbstractPortValue> port : listPorts) {
            OutputPort currentOutput = new OutputPort(run, name, depth, (AbstractPortValue) port);

            setOutput(currentOutput,true);
        }
    }
}
