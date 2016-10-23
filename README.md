# Multi-Files-Parser
Multi-File Parser is having ability to parse XML, JSON, XLS and CSV. It is written on top of various industry standard parsers. 
The API is **simple to use**, independent in nature and is written to **perform and scale**.

- It is written using Strategy so new strategies i.e. Parsers for new formats can be added adhering Single Responsibility.
- Ability to read files from a directory or File[].
- Supports paralellism i.e. processes files in parallel using Java 7 Fork/Join.
- Easy data validation support using JSR-303 Bean Validation annotations.
- Uses Notification Pattern ([bliki](http://martinfowler.com/eaaDev/Notification.html)) for validation errors.
- Spring Support

##Usage
Simple usage with a single line of code.

Example XML Parse Example. **Check for all examples in [test] (https://github.com/dapinder-dhillon/Multi-Files-Parser/tree/master/src/test) folder** :+1:.

```
@Autowired
private DataFeedController dataFeedController;
....
..
Map<String, Records> rtnObj = dataFeedController.process(Records.class, sourceDir, FeedSupportedFileTypes.XML);

if (!CollectionUtils.isEmpty(rtnObj)) {
				final Notification notification = dataFeedController.validate(Records.class, 
                                                     FeedSupportedFileTypes.XML,	rtnObj);
				if (notification.hasErrors()) {
					throw new MyCustomException(notification.errorMessage());
				}
				
				Set<String> keys = rtnObj.keySet();
				for (String key : keys) {
					Records recs = rtnObj.get(key);
					if (!CollectionUtils.isEmpty(recs.getRecords())) {
						for (Record record : recs.getRecords()) {
							//Process Records..
						}
					}
			  }
				
}
```
##POJO Class definition
```
@XmlRootElement(name = "records")
public class Records {

	private List<Record> records;

....
....


....

@XmlRootElement(name = "record")
public class Record {

	@NotNull
	@Size(max = 3)
	@PropertyLabel(value="Origin")
	private String orgn;
	private String dstn;
	private String airtcraftType;
...
.....

```

##Building Multi-File Parser from Source Code
[Gradle](https://gradle.org/) is used to build Multi-File parser.
- git clone [https://github.com/dapinder-dhillon/Multi-Files-Parser] (https://github.com/dapinder-dhillon/Multi-Files-Parser)
- gradle clean b

##License
This projected is licensed under the terms of the [Apache v2 license](http://www.apache.org/licenses/LICENSE-2.0.html)
