package agora.beet.model;

import agora.beet.util.ArrayNestingManager;
import agora.beet.util.JSONManager;
import agora.beet.util.TestCaseFileManager;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static agora.beet.main.GenerateDeclsFile.*;

public class DeclsExit {

    private String packageName;
    private String endpoint;
    private String operationName;
    private String statusCode;
    private String variableNameInput;
    private int exitNumber;
    private DeclsVariable enterDeclsVariables;
    private DeclsVariable exitDeclsVariables;
    private String nameSuffix;
    private boolean isNestedArray;  // Bad practice

    // Used when the exit is of type object
    public DeclsExit(String packageName, String endpoint, String operationName, String variableNameInput,
                     DeclsVariable enterVariables, String variableNameOutput, Schema mapOfProperties, String nameSuffix, String statusCode) {
        this.packageName = packageName;
        this.endpoint = endpoint;
        this.operationName = operationName;
        this.variableNameInput = variableNameInput;
        this.nameSuffix = nameSuffix;
        this.statusCode = statusCode;

        this.exitNumber = numberOfExits;
        numberOfExits = numberOfExits + 1;

        this.enterDeclsVariables = enterVariables;
        this.isNestedArray = false;

        this.exitDeclsVariables = DeclsVariable.generateDeclsVariablesOfOutput("return", "return", packageName,
                variableNameOutput + nameSuffix, mapOfProperties);
    }

    // Used when the exit is of type array (bad practice)
    public DeclsExit(String packageName, String endpoint, String operationName, String variableNameInput,
                     DeclsVariable enterVariables, String variableNameOutput, ArraySchema arraySchema, String nameSuffix, String statusCode) {

        this.packageName = packageName;
        this.endpoint = endpoint;
        this.operationName = operationName;
        this.variableNameInput = variableNameInput;
        this.nameSuffix = nameSuffix;
        this.statusCode = statusCode;

        this.exitNumber = numberOfExits;
        numberOfExits = numberOfExits + 1;

        this.enterDeclsVariables = enterVariables;
        this.isNestedArray = true;

        this.exitDeclsVariables = DeclsVariable.generateDeclsVariablesOfArrayOutput(arraySchema, variableNameOutput + nameSuffix, "return", "return");

    }

    // Used when the exit is primitive (Bad pracice)
    public DeclsExit(String packageName, String endpoint, String operationName, String variableNameInput,
                     DeclsVariable enterVariables, String variableNameOutput, String parameterType, String statusCode) {
        this.packageName = packageName;
        this.endpoint = endpoint;
        this.operationName = operationName;
        this.variableNameInput = variableNameInput;
        this.nameSuffix = "";
        this.statusCode = statusCode;

        this.exitNumber = numberOfExits;
        numberOfExits = numberOfExits + 1;

        this.enterDeclsVariables = enterVariables;
        this.isNestedArray = false;

        this.exitDeclsVariables = DeclsVariable.generateDeclsVariablesOfPrimitiveResponse(parameterType, variableNameOutput, "return", "return");
    }

    public String getExitName() {
        return this.packageName + "." + this.endpoint + "." + this.operationName + HIERARCHY_SEPARATOR + this.statusCode +  this.nameSuffix + "(" +
                this.packageName + "." + this.variableNameInput + ")";
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getVariableNameInput() {
        return variableNameInput;
    }

    public void setVariableNameInput(String variableNameInput) {
        this.variableNameInput = variableNameInput;
    }

    public int getExitNumber() {
        return exitNumber;
    }

    public void setExitNumber(int exitNumber) {
        this.exitNumber = exitNumber;
    }

    public DeclsVariable getEnterDeclsVariables() {
        return enterDeclsVariables;
    }

    public void setEnterDeclsVariables(DeclsVariable enterDeclsVariables) {
        this.enterDeclsVariables = enterDeclsVariables;
    }

    public DeclsVariable getExitDeclsVariables() {
        return exitDeclsVariables;
    }

    public void setExitDeclsVariables(DeclsVariable exitDeclsVariables) {
        this.exitDeclsVariables = exitDeclsVariables;
    }

    public boolean isNestedArray() {
        return isNestedArray;
    }

    public void setNestedArray(boolean nestedArray) {
        isNestedArray = nestedArray;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public String toString() {
        String res = "ppt " + this.getExitName() + ":::EXIT" + exitNumber + "\n" +
                "ppt-type subexit";

        res = res + "\n" + enterDeclsVariables;

        res = res + "\n" + exitDeclsVariables;

        return res;
    }

    public String generateDtrace(TestCase testCase, DeclsEnter declsEnter) {
        String res = "";

        String responseBody = testCase.getResponseBody();

        // If the exit is of type array (Bad practice)
        if(JSONManager.isStringJsonArray(responseBody)) {
            JSONArray jsonArray = JSONManager.stringToJsonArray(responseBody);

            if(this.isNestedArray) {    // (Bad practice) If the response is parseable to array and the exit is of type nestedArray
                // Count the number of arrays (Nesting level)
                int targetNestingLevel = (int) Arrays.stream(this.getNameSuffix().split("\\.")).filter(x-> x.equalsIgnoreCase("array")).count();
                // Count the number of arrays corresponding to the number of .arrays and return the dtrace
                try {
                    List<JSONArray> jsonArraysToGenerateDtrace = ArrayNestingManager.getJSONArraysOfSpecifiedNestingLevel(jsonArray, targetNestingLevel, 1);

                    // For all the elements of the list of jsonArrays, generate a dtrace
                    for(JSONArray element: jsonArraysToGenerateDtrace) {
                        res = res + this.generateSingleDtraceEnterAndExitArray(element, testCase, declsEnter);
                    }

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }


            } else {                    // (Bad practice) If the response is parseable to array but the exit is not of type nestedArray (i.e., we are generating a dtrace for one of its elements)
                // Remove all the arrays until reaching an object and continue with normal behaviour
                // Flatten the elements of the nested arrays
                List<JSONObject> flatList = ArrayNestingManager.doBubbleSort(jsonArray);
                // Write in res, generate dtraces from the list
                res = res + this.generateSingleDtraceEnterAndExit(flatList, testCase, declsEnter);

            }

        } else {        // If the response is parseable to JSONObject (Expected practice)
            // Expected behaviour
            JSONObject json = JSONManager.stringToJsonObject(responseBody);
            res = res + this.generateSingleDtraceEnterAndExit(Collections.singletonList(json), testCase, declsEnter);
        }

        return res;

    }

    private String generateSingleDtraceEnterAndExit(List<JSONObject> jsonObjectList, TestCase testCase, DeclsEnter declsEnter){
        String res = "";

        for(JSONObject json: jsonObjectList) {

            // Name suffix
            List<String> elementRoute = Arrays.stream(this.nameSuffix.split(HIERARCHY_SEPARATOR))
                    .filter(e -> e.trim().length() > 0)
                    .collect(Collectors.toList());


            // The elements of this list can be either JSONArray (If there are elements with nested arrays) or JSONObject
            List<Object> nestedJsonObjects = new ArrayList<>();

            if(elementRoute.isEmpty()){
                nestedJsonObjects.add(json);
            } else {
                nestedJsonObjects = getListOfJsonElementsForDeclsExit(json, elementRoute);
            }

            for(Object jsonElement: nestedJsonObjects){
                // There must be one Decls Enter per DeclsExit
                if(jsonElement instanceof JSONObject) {     // If the element is of type JSONObject
                    res = res + declsEnter.generateDtrace(testCase) + "\n";                         // DeclsEnter
                    res = res + this.generateSingleDtraceExit(testCase, (JSONObject) jsonElement);   // DeclsExit
                } else {    // If the element is of type JSONArray
                    // This function prints both the DeclsEnter and the DeclsExit
                    res = res + this.generateSingleDtraceEnterAndExitArray((JSONArray) jsonElement, testCase, declsEnter);
                }

            }

        }

        return res;

    }

    private String generateSingleDtraceEnterAndExitArray(JSONArray jsonArray, TestCase testCase, DeclsEnter declsEnter) {
        // The JSONArray is the required one
        String res = "";

        res = res + declsEnter.generateDtrace(testCase) + "\n";
        res = res + this.generateSingleDtraceExitArray(testCase, jsonArray);
        return res;
    }

    /**
     *
     * @param testCase
     * @param jsonArray
     * @return A string representing the dtrace of an array of JSON Objects. If the value of the array is null, its hashcode is null and its value is set to nonsensical. Example:
     * return.markets
     * null
     * 1
     * return.markets[..]
     * nonsensical
     * 2
     *
     */
    public String generateSingleDtraceExitArray(TestCase testCase, JSONArray jsonArray) {

        String res = this.getExitName() + ":::EXIT" + exitNumber;

        res = res + "\n" + enterDeclsVariables.generateDtraceEnter(testCase) + "\n";

        // Generate the dtrace exit for the array
        // Group 1
        res = res + "return" + "\n";
        String v = "\"" + testCase.getTestCaseId() + this.operationName + "returnoutput" + "\"";
        v = v.replace(HIERARCHY_SEPARATOR, "").replace("_", "");
        res = res + Math.abs(v.hashCode()) + "\n";
        res = res + "1" + "\n";

        // Returns the value of the array or nonsensical if the array is null
        String value = generateDtraceExitValueOfJSONArray(testCase, jsonArray, this.exitDeclsVariables.getEnclosedVariables().get(0).getDecType(), "array");

        // Group 2
        res = res + "return.array" + "\n";
        if(value.equals("nonsensical")) {
            res = res + "null" + "\n";
        } else {
            String v1 = "\"" + testCase.getTestCaseId() + this.operationName + "return" + this.nameSuffix + "output" + "\"";
            v1 = v1.replace(HIERARCHY_SEPARATOR, "").replace("_", "");
            res = res + Math.abs(v1.hashCode()) + "\n";
        }
        res = res + "1" + "\n";

        // Group 3
        res = res + "return.array[..]" + "\n";
        // The declaration type of the first enclosed variable will always reveal the type of the elements of the array
        res = res + value + "\n";
        String modified = "1";
        if(value.equals("nonsensical")) {
            modified = "2";
        }
        res = res + modified + "\n\n";

        return res;

    }

    public String generateSingleDtraceExit(TestCase testCase, JSONObject jsonElement) {
        String res = this.getExitName() + ":::EXIT" + exitNumber;

        res = res + "\n" + enterDeclsVariables.generateDtraceEnter(testCase);

        res = res + "\n" + this.exitDeclsVariables.generateDtraceExit(testCase, jsonElement, false) + "\n\n";

        return res;

    }

    public static String generateDtraceExitValueOfJSONArray(TestCase testCase, JSONArray elements, String dectype, String variableName) {
        String value = "nonsensical";

        // If elements == null, the elements are set to nonsensical
        if(elements != null){
            if(primitiveTypes.contains(dectype.replace("[]", ""))) { // If array of primitives
                boolean isString = false;
                if(dectype.replace("[]", "").equals(STRING_TYPE_NAME)) {
                    isString = true;
                }
                value = "";
                for(int i = 0; i < elements.size(); i++) {

                    if(isString) {
                        if(elements.get(i) == null || Arrays.asList(stringsToConsiderAsNull).contains(elements.get(i))) {
                            value = value + " " + null;
                        } else {
                            value = value + " \"" + TestCaseFileManager.removeNewLineChars((String) elements.get(i)) + "\"";
                        }
                    } else {
                        value = value + " " + elements.get(i);
                    }
                }

                value = "[" + value.trim() + "]";

            } else {    // If array of objects
                String hashcode = "";
                for(int i = 1; i <= elements.size(); i++) {
                    if(elements.get(i-1) != null) {
                        String v = "\"" + testCase.getTestCaseId() + HIERARCHY_SEPARATOR + variableName.replace("[..]", "") + HIERARCHY_SEPARATOR + "output" + HIERARCHY_SEPARATOR + i + "\"";
                        v = v.replace(HIERARCHY_SEPARATOR, "").replace("_", "");
                        hashcode = hashcode + Math.abs(v.hashCode()) + " ";
                    } else {
                        hashcode = hashcode + "null ";
                    }

                }

                value = "[" + hashcode.trim() + "]";
            }
        }
        return value;
    }


    public static List<Object> getListOfJsonElementsForDeclsExit(JSONObject json, List<String> elementRoute){

        List<Object> res = new ArrayList<>();

        String element = elementRoute.get(0);

        if(json == null) {
            throw new NullPointerException("The response of the test case cannot be null");
        }

        // If the target element is a nested array
        List<String> elementArrayRoute = Arrays.stream(element.split("\\."))
                .filter(e -> e.trim().length() > 0)
                .collect(Collectors.toList());
        // If the target element is a nested array
        if(elementArrayRoute.size() > 1) {
            // Get the nested arrays (i.e., value of the element)
            JSONArray jsonArray = (JSONArray) json.get(elementArrayRoute.get(0));
            // We increase the targetNestingLevel by one because the first level of nesting is already present in the father exit
            int targetNestingLevel = (int) Arrays.stream(element.split("\\.")).filter(x->x.equalsIgnoreCase("array")).count() + 1;
            List<JSONArray> jsonArraysToGenerateDtrace = ArrayNestingManager.getJSONArraysOfSpecifiedNestingLevel(jsonArray, targetNestingLevel, 1);
            res.addAll(jsonArraysToGenerateDtrace);

            return res;
        }

        Object jsonSon = json.get(element);

        if(jsonSon instanceof  JSONObject) {        // If jsonSon is of type JSONObject
            JSONObject jsonSonObject = (JSONObject) jsonSon;
            if(elementRoute.size() == 1) {      // If element is the last element of elementRoute (i.e. is the object we are looking for)
                res.add(jsonSonObject.get(element));    // Add JSONObject
            } else {
                // Recursive call if element is not the last element of elementRoute
                res.addAll(getListOfJsonElementsForDeclsExit(jsonSonObject, elementRoute.subList(1, elementRoute.size())));
            }
        } else if(jsonSon instanceof JSONArray) {   // If jsonSon is of type JSONArray
            JSONArray jsonSonArray = (JSONArray) jsonSon;

            // Iterate over all elements of the JSONArray
            for(int i = 0; i < jsonSonArray.size(); i++) {
                Object jsonSonElement = jsonSonArray.get(i);

                if(jsonSonElement instanceof JSONObject) {
                    if(elementRoute.size()==1){
                        res.add(jsonSonElement);       // Add JSONObject
                    } else {
                        res.addAll(getListOfJsonElementsForDeclsExit((JSONObject) jsonSonElement, elementRoute.subList(1, elementRoute.size())));
                    }
                } else if(jsonSonElement instanceof JSONArray) {        // This condition is reached when we are dealing with a nested array
                    // Get all the properties of the nested array
                    List<JSONObject> flatList = ArrayNestingManager.doBubbleSort((JSONArray) jsonSonElement);
                    res.addAll(flatList);
                }
            }

        }

        return res;

    }

}
