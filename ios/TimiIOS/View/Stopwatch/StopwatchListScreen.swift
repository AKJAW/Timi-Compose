import SwiftUI
import shared
import Combine
import KMPNativeCoroutinesCombine

class StopwatchListPublisher: ObservableObject {
    private var cancellables = Set<AnyCancellable>()
    let viewModel: StopwatchViewModel =
    KoinWrapper.get(type: StopwatchViewModel.self)
    
    @Published
    var availableTasks: [Task] = []
    
    @Published
    var stopwatches: [Task : String] = [:]
    
    init() {
        stopwatches = viewModel.stopwatchesNativeValue
        createPublisher(for: viewModel.availableTasksNative)
            .receive(on: RunLoop.main)
            .sink { completion in
                print("Received completion availableTasksNative: \(completion)")
            } receiveValue: { tasks in
                self.availableTasks = tasks
            }
            .store(in: &cancellables)
        
        createPublisher(for: viewModel.stopwatchesNative)
            .receive(on: RunLoop.main)
            .sink { completion in
                print("Received completion stopwatchesNative: \(completion)")
            } receiveValue: { stopwatches in
                self.stopwatches = stopwatches
            }
            .store(in: &cancellables)
    }
    
    func addStopwatch() {
        guard let task = availableTasks.first else { return }
        print("Adding: \(task)")
        viewModel.start(task: task)
    }
}

struct StopwatchListScreen: View {
    
    @ObservedObject private var publisher = StopwatchListPublisher()
    
    var body: some View {
        List {
            ForEach(
                Array(publisher.stopwatches.keys).sorted { $0.id < $1.id },
                id: \.self
            ) { task in
                StopwatchItem(
                    task: task,
                    time: publisher.stopwatches[task]!,
                    onStart: { publisher.viewModel.start(task: task) },
                    onPause: { publisher.viewModel.pause(task: task) },
                    onStop: { publisher.viewModel.stop(task: task) }
                )
            }
            // TODO is this really the way?
            HStack {
                Spacer()
                Text("Add stopwatch")
                    .onTapGesture {
                        publisher.addStopwatch()
                    }
                Spacer()
            }
        }
    }
}

struct StopwatchItem: View {
    var task: Task
    var time: String
    var onStart: () -> Void
    var onPause: () -> Void
    var onStop: () -> Void
    
    var body: some View {
        VStack(spacing: 0) {
            HStack {
                Text(task.name)
                Spacer()
                Text(time)
            }
            .padding(8)
            HStack {
                Spacer()
                StopwatchAction(
                    iconName: "play.fill",
                    onClick: onStart
                )
                StopwatchAction(
                    iconName: "pause.fill",
                    onClick: onPause
                )
                StopwatchAction(
                    iconName: "stop.fill",
                    onClick: onStop
                )
            }
        }
    }
}

struct StopwatchAction: View {
    var iconName: String
    // TODO color
    var onClick: () -> Void
    
    var body: some View {
        ZStack {
            Image(systemName: iconName)
                .resizable()
                .scaledToFit()
                .frame(width: 20, height: 20)
        }
        .padding(8)
        .onTapGesture {
            onClick()
        }
    }
}

struct StopwatchListScreen_Previews: PreviewProvider {
    static var previews: some View {
        StopwatchListScreen()
    }
}
